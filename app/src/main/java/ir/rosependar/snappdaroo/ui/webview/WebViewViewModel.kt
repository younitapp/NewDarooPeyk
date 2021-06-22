package ir.rosependar.snappdaroo.ui.webview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebViewViewModel : ViewModel() {

    val isPermissionGranted = MutableLiveData(false)

}