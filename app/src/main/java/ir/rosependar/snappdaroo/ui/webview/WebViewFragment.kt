package ir.rosependar.snappdaroo.ui.webview

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ir.rosependar.snappdaroo.utils.l
import kotlinx.android.synthetic.main.web_view_fragment.*


import android.os.Environment
import ir.rosependar.snappdaroo.R
import java.io.File
import java.io.IOException;

class WebViewFragment : Fragment() {

    companion object {
        fun newInstance() = WebViewFragment()
    }

    private val url by lazy { arguments?.getString("url") }
    private lateinit var viewModel: WebViewViewModel
    private val REQUEST_PERMISSION_CODE = 111

    private val FILECHOOSER_RESULTCODE = 1
    private var mUploadMessage: ValueCallback<Uri?>? = null
    private var mCapturedImageURI: Uri? = null

    // the same for Android 5.0 methods only
    private var mFilePathCallback: ValueCallback<Array<Uri?>?>? = null
    private var mCameraPhotoPath: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.web_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }
        try {
            if (checkPermissionFromDevice()) setupWebView()
            else requestPermission()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupWebView() {
        webViewMain.settings.javaScriptEnabled = true
        webViewMain.visibility = View.INVISIBLE
        webViewMain.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                try {
                    animationView.visibility = View.GONE
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    webViewMain.visibility = View.VISIBLE
                    txt_title.text = webViewMain.title
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

        webViewMain.webChromeClient = object : WebChromeClient() {
            // for Lollipop, all in one
            override fun onShowFileChooser(
                webView: WebView?, filePathCallback: ValueCallback<Array<Uri?>?>,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                if (mFilePathCallback != null) {
                    mFilePathCallback!!.onReceiveValue(null)
                }
                mFilePathCallback = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(activity!!.packageManager) != null) {

                    // create the file where the photo should go
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        l("Unable to create Image File $ex")
                    }

                    // continue only if the file was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.absolutePath
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "image/*"
                val intentArray: Array<Intent?> =
                    takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_chooser))
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE)
                return true
            }

            // creating image files (Lollipop only)
            @Throws(IOException::class)
            private fun createImageFile(): File {
                var imageStorageDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "DirectoryNameHere"
                )
                if (!imageStorageDir.exists()) {
                    imageStorageDir.mkdirs()
                }

                // create an image file name
                imageStorageDir = File(
                    imageStorageDir.toString() + File.separator.toString() + "IMG_" + System.currentTimeMillis()
                        .toString() + ".jpg"
                )
                return imageStorageDir
            }

            // openFileChooser for Android 3.0+
            fun openFileChooser(uploadMsg: ValueCallback<Uri?>, acceptType: String?) {
                mUploadMessage = uploadMsg
                try {
                    val imageStorageDir = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "DirectoryNameHere"
                    )
                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs()
                    }
                    val file = File(
                        imageStorageDir.toString() + File.separator.toString() + "IMG_" + System.currentTimeMillis()
                            .toString() + ".jpg"
                    )
                    mCapturedImageURI = Uri.fromFile(file) // save to the private variable
                    val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI)
                    // captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    val i = Intent(Intent.ACTION_GET_CONTENT)
                    i.addCategory(Intent.CATEGORY_OPENABLE)
                    i.type = "image/*"
                    val chooserIntent = Intent.createChooser(i, getString(R.string.image_chooser))
                    chooserIntent.putExtra(
                        Intent.EXTRA_INITIAL_INTENTS,
                        arrayOf<Parcelable>(captureIntent)
                    )
                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE)
                } catch (e: java.lang.Exception) {
                    Toast.makeText(requireContext(), "Camera Exception:$e", Toast.LENGTH_LONG)
                        .show()
                }
            }

            // openFileChooser for Android < 3.0
            fun openFileChooser(uploadMsg: ValueCallback<Uri?>) {
                openFileChooser(uploadMsg, "")
            }

            // openFileChooser for other Android versions
            /* may not work on KitKat due to lack of implementation of openFileChooser() or onShowFileChooser()
               https://code.google.com/p/android/issues/detail?id=62220
               however newer versions of KitKat fixed it on some devices */
            fun openFileChooser(
                uploadMsg: ValueCallback<Uri?>,
                acceptType: String?,
                capture: String?
            ) {
                openFileChooser(uploadMsg, acceptType)
            }
        }

        l("milUrl $url")
        webViewMain.loadUrl(url)
    }


    // return here when file selected from camera or from SD Card
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // start of code for Lollipop only
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != FILECHOOSER_RESULTCODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri?>? = null

            // check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null || data.data == null) {
                    // if there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = arrayOf(Uri.parse(mCameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
            mFilePathCallback!!.onReceiveValue(results)
            mFilePathCallback = null
        } // end of code for Lollipop only
    }


    private fun checkPermissionFromDevice(): Boolean {

        val writeExternalStorageResult = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val readExternalStorageResult = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val recordAudioResult =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)

        return writeExternalStorageResult == PackageManager.PERMISSION_GRANTED &&
                recordAudioResult == PackageManager.PERMISSION_GRANTED &&
                readExternalStorageResult == PackageManager.PERMISSION_GRANTED

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), REQUEST_PERMISSION_CODE
        )
        setupWebView()
    }

}