package ir.rosependar.snappdaroo.ui.webview

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.utils.l
import kotlinx.android.synthetic.main.web_view_fragment.*

class WebViewFragment : Fragment() {

    companion object {
        fun newInstance() = WebViewFragment()
    }

    private val url by lazy { arguments?.getString("url") }
    private lateinit var viewModel: WebViewViewModel
    private val REQUEST_PERMISSION_CODE = 111


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
        l("milUrl $url")
        webViewMain.loadUrl(url)
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