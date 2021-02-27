package ir.rosependar.snappdaroo.ui.webview

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import ir.rosependar.snappdaroo.R
import kotlinx.android.synthetic.main.web_view_fragment.*

class WebViewFragment : Fragment() {

    companion object {
        fun newInstance() = WebViewFragment()
    }

    private val url by lazy { arguments?.getString("url") }
    private lateinit var viewModel: WebViewViewModel

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
            setupWebView()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setupWebView() {
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
        webViewMain.loadUrl(url)
    }

}