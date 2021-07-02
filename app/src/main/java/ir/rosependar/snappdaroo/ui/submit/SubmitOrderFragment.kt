package ir.rosependar.snappdaroo.ui.submit

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.dialogs.SelectImageDialog
import ir.rosependar.snappdaroo.dialogs.SelectImageListener
import ir.rosependar.snappdaroo.dialogs.TimePicker
import ir.rosependar.snappdaroo.dialogs.TimePickerListener
import ir.rosependar.snappdaroo.models.CodeName
import ir.rosependar.snappdaroo.utils.*
import kotlinx.android.synthetic.main.submit_order_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.ext.getScopeId
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SubmitOrderFragment : Fragment() {

    private var isCamera: Boolean = false
    private var mImageBitmap: Bitmap? = null
    private var mCurrentPhotoPath: String? = null
    private val type by lazy { requireArguments().getInt("type_id") }

    companion object {
        fun newInstance() = SubmitOrderFragment()
    }

    private var filePath: String = ""
    private val viewModel: SubmitOrderViewModel by viewModel()
    private var deliveryTimes = mutableListOf<CodeName>()
    private var selectedTimeId: Int? = null
    val loadingDialog by lazy { CustomProgressDialog().show(requireContext(), "در حال ارسال") }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.submit_order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.settingsData.observe(viewLifecycleOwner, {
            deliveryTimes = it.app_prs_delivery_time.toMutableList()
        })
        initViews()
    }

    private fun initViews() {
        l("type id is : $type")
        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }
        btn_add_image.setOnClickListener {
            imagePickerSetup()
        }
        crd_image_picker.setOnClickListener {
            imagePickerSetup()
        }
        rl_time_picker.setOnClickListener {
            l(deliveryTimes.toString())
            TimePicker.newInstance(deliveryTimes).show(parentFragmentManager, "")
            TimePicker.timeListener = object : TimePickerListener {
                override fun OnTimePickerClicked(time: CodeName) {
                    rv_time.text = time.name
                    selectedTimeId = time.code
                }
            }
        }
        btn_submit.setOnClickListener {
            if (selectedTimeId == null) {
                errorToast("لطفا زمان ارسال را انتخاب کنید.")
                return@setOnClickListener
            }
            if (edt_prescription.editText!!.text.toString().isEmpty() && filePath.isEmpty()) {
                errorToast("لطفا متن نسخه یا تصویر نسخه را بررسی کنید.")
                return@setOnClickListener
            }
            if (filePath.isEmpty()) {
                AlertDialog.Builder(requireContext())
                    .setTitle("ارسال بدون تصویر")
                    .setMessage("آیا از ارسال نسخه بدون تصویر مطمئن هستید ؟ جهت تسهیل روند پردازش درخواست شما پیشنهاد می شود حتما تصویر نسخه را ارسال کنید.")
                    .setPositiveButton(
                        "ارسال بدون تصویر"
                    ) { dialog, which ->
                        dialog.dismiss()
                        sendFile()
                    }
                    .setNegativeButton("تصویر را ارسال می کنم") { dialog, which -> dialog.dismiss() }
                    .show()
                return@setOnClickListener
            }
            sendFile()
        }
    }


    private fun imagePickerSetup() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    SelectImageDialog.newInstance().show(parentFragmentManager, "")
                    SelectImageDialog.listener = object : SelectImageListener {
                        override fun OnBtnCameraClicked() {
                            dispatchTakePictureIntent()
                        }

                        override fun OnBtnGalleryClicked() {
                            val intent = Intent()
                            intent.type = "image/*"
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(
                                Intent.createChooser(
                                    intent,
                                    "انتخاب تصویر نسخه"
                                ), 550
                            )


                        }
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }
            }).check()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = "/$absolutePath"
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "ir.rosependar.snappdaroo.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 600)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 550 && resultCode == RESULT_OK) {
            mImageBitmap = MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                data!!.data!!
            )
            try {
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "JPEG_${timeStamp}_.jpg"
                )
                FileOutputStream(file).use { out ->
                    mImageBitmap!!.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        out
                    ) // bmp is your Bitmap instance
                    out.flush()
                    out.close()
                }
                filePath = file.path

            } catch (e: IOException) {
                e.printStackTrace()
            }
            crd_image_picker!!.background = BitmapDrawable(resources, mImageBitmap)
            lyt_image_picker.visibility = View.GONE
            isCamera = false

        } else if (requestCode == 600 && resultCode == RESULT_OK) {

            mImageBitmap = MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                Uri.parse("file:/$mCurrentPhotoPath")
            )
            crd_image_picker!!.background = BitmapDrawable(resources, mImageBitmap)
            lyt_image_picker.visibility = View.GONE
            filePath = mCurrentPhotoPath!!
            isCamera = true
        }
    }

    private fun sendFile() {
        var fileUri: Uri? = if (isCamera)
            Uri.parse("file:/$filePath")
        else {
            Uri.parse("file://$filePath")
        }

        GlobalScope.launch(Dispatchers.Main) {

            if (filePath.isEmpty()) {
                MultipartUploadRequest(
                    requireContext(),
                    serverUrl = Constants.BASE_URL + "rpsapi_order"
                )
                    .setMethod("POST")
                    .addParameter("device_id", Prefs.getInstance()!!.getDeviceId())
                    .addParameter("api_token", Prefs.getInstance()!!.getToken())
                    .addParameter("delivery_time", selectedTimeId!!.toString())
                    .addParameter("kind", type.toString())
                    .addParameter("prescription_text", edt_prescription.editText!!.text.toString())
                    .addParameter("comments", edt_comment.editText!!.text.toString())

                    .subscribe(
                        requireContext(),
                        viewLifecycleOwner,
                        object : RequestObserverDelegate {
                            override fun onCompleted(
                                context: Context,
                                uploadInfo: UploadInfo
                            ) {
                                val notificationManager: NotificationManager =
                                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.cancelAll()
                            }

                            override fun onCompletedWhileNotObserving() {

                            }

                            override fun onError(
                                context: Context,
                                uploadInfo: UploadInfo,
                                exception: Throwable
                            ) {
                                loadingDialog.dismiss()
                                exception.printStackTrace()
                                /*loadingDialog.dialog.dismiss()*/
                                errorToast(exception.localizedMessage!!)
                            }

                            override fun onProgress(
                                context: Context,
                                uploadInfo: UploadInfo
                            ) {

                            }

                            override fun onSuccess(
                                context: Context,
                                uploadInfo: UploadInfo,
                                serverResponse: ServerResponse
                            ) {
                                loadingDialog.dismiss()
                                /*loadingDialog.dialog.dismiss()*/
                                l("Response is ${serverResponse.bodyString}")
                                successToast("نسخه شما با موفقیت ثبت شد.")
                                findNavController().popBackStack()
                            }

                        })
            } else {
                val compressedFile =
                    Constants.compressImage(requireContext(), File(fileUri!!.path!!))
                MultipartUploadRequest(
                    requireContext(),
                    serverUrl = Constants.BASE_URL + "rpsapi_order"
                )
                    .setMethod("POST")
                    .addParameter("device_id", Prefs.getInstance()!!.getDeviceId())
                    .addParameter("api_token", Prefs.getInstance()!!.getToken())
                    .addParameter(
                        "delivery_time",
                        selectedTimeId!!.toString()
                    )
                    .addFileToUpload(
                        filePath = compressedFile.path,
                        parameterName = "image_files[]"
                    )
                    .addParameter("kind", type.toString())
                    .addParameter("prescription_text", edt_prescription.editText!!.text.toString())
                    .addParameter("comments", edt_comment.editText!!.text.toString())

                    .subscribe(
                        requireContext(),
                        viewLifecycleOwner,
                        object : RequestObserverDelegate {
                            override fun onCompleted(
                                context: Context,
                                uploadInfo: UploadInfo
                            ) {
                                val notificationManager: NotificationManager =
                                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.cancelAll()
                            }

                            override fun onCompletedWhileNotObserving() {

                            }

                            override fun onError(
                                context: Context,
                                uploadInfo: UploadInfo,
                                exception: Throwable
                            ) {
                                loadingDialog.dismiss()
                                exception.printStackTrace()
                                /*loadingDialog.dialog.dismiss()*/
                                errorToast(exception.localizedMessage!!)
                            }

                            override fun onProgress(
                                context: Context,
                                uploadInfo: UploadInfo
                            ) {
                                loadingDialog.setTitle("در حال ارسال ${uploadInfo.progressPercent}%")
                            }

                            override fun onSuccess(
                                context: Context,
                                uploadInfo: UploadInfo,
                                serverResponse: ServerResponse
                            ) {


                                loadingDialog.dismiss()
                                l("Response is ${serverResponse.bodyString}")
                                successToast("نسخه شما با موفقیت ثبت شد.")
                                findNavController().popBackStack()
                            }

                        })
            }
        }

    }
}