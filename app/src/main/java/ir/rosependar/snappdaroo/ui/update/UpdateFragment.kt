package ir.rosependar.snappdaroo.ui.update

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.utils.errorToast
import ir.rosependar.snappdaroo.utils.l
import kotlinx.android.synthetic.main.update_app_fragment.*
import java.io.File

class UpdateAppFragment : DialogFragment() {
    var url: String? = ""
    var version: Long? = 0
    val ARG_URL = "ARG_URL"
    val ARG_VERSION = "ARG_VERSION"
    private val PROVIDER_PATH = "ir.snappdaroo.provider"
    private val APP_INSTALL_PATH = "\"application/vnd.android.package-archive\""
    private val FILE_BASE_PATH = "file://"

    companion object {
        fun newInstance(
            urlToUpdate: String,
            api_version : Long
        ) = UpdateAppFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_URL, urlToUpdate)
                putLong(ARG_VERSION, api_version)
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_app_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window
            ?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_URL)
            version = it.getLong(ARG_VERSION)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_update.setOnClickListener {
            if (url!!.contains(".apk")) {
                Dexter.withActivity(activity)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                            downloadApk(url!!, version!!)
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) { /* ... */
                            errorToast("بدون دسترسی به فایل امکان دانلود آپدیت جدید وجود ندارد.")
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {
                        }
                    }).check()
            } else {
                val i = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                }
                requireContext().startActivity(i)
            }
        }
    }

    private fun downloadApk(url: String, version: Long) {
        btn_update.visibility = View.INVISIBLE
        circularProgressBar2.visibility = View.VISIBLE
        circularProgressBar2.progressMax = 100f
        PRDownloader.download(
            url,
            requireContext().getExternalFilesDir(null)?.absolutePath + "/files/apk",
            "younit-$version.apk"
        ).build()
            .setOnProgressListener {
                circularProgressBar2.progress = ((it.currentBytes * 100) / 11000000).toFloat()
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    l("Download Complete")
                    btn_update.visibility = View.VISIBLE
                    circularProgressBar2.visibility = View.GONE
                    val file = File(
                        requireContext().getExternalFilesDir(null)?.absolutePath + "/files/apk",
                        "younit-$version.apk"
                    )
                    if (file.exists()) {
                        l("Opening Installer")
                        showInstallOption(file)
                    }
                }

                override fun onError(error: com.downloader.Error?) {
                    btn_update.visibility = View.VISIBLE
                    circularProgressBar2.visibility = View.GONE
                    errorToast("خطا در دانلود فایل")
                }


            })
    }

    private fun showInstallOption(
        file: File
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(
                requireContext(),
                PROVIDER_PATH,
                file
            )
            val install = Intent(Intent.ACTION_VIEW)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            install.data = contentUri
            requireContext().startActivity(install)
            // finish()
        } else {
            val uri = Uri.fromFile(file)

            val install = Intent(Intent.ACTION_VIEW)
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            install.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            requireContext().startActivity(install)
            // finish()
        }

    }

}