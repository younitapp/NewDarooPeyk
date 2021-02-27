package ir.rosependar.snappdaroo.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.andrognito.flashbar.Flashbar
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import ir.rosependar.snappdaroo.MainActivity
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.ui.update.UpdateAppFragment
import ir.rosependar.snappdaroo.utils.Constants
import ir.rosependar.snappdaroo.utils.Constants.Companion.API_VERSION
import ir.rosependar.snappdaroo.utils.Prefs
import ir.rosependar.snappdaroo.utils.l
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    val splashViewModel: SplashViewModel by viewModel()
    private var paidOrderId = ""
    private var notifOrderId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data: Uri? = intent.data
        if (intent.getStringExtra("order_id") != null)
            notifOrderId = intent.getStringExtra("order_id")!!
        setContentView(R.layout.activity_splash)

        if (data?.scheme == "daroopeyk" && data.host == "daroopeyk.app") {

            if (data.path != null && data.path!!.isNotEmpty()) {
                paidOrderId = data.path!!.replace("/", "")
            }
        }
        if (data?.scheme == "https" && data.host == "testehoosh.ir") {

            if (data.path != null && data.path!!.isNotEmpty()) {
                paidOrderId = data.path!!.replace("/refer/", "")
            }
        }
        fetchData()

    }

    fun fetchData() {
        splashViewModel.splashData.observe(this, Observer { settingsResponse ->
            if (settingsResponse?.body() != null && settingsResponse.isSuccessful) {
                if (API_VERSION < settingsResponse.body()!!.data.api_version) {
                    UpdateAppFragment.newInstance(
                        settingsResponse.body()!!.data.app_url_android,
                        settingsResponse.body()!!.data.api_version.toLong()
                    ).apply {
                        isCancelable = false
                        show(supportFragmentManager, "update")
                    }
                    return@Observer
                }

                splashViewModel.updateDataFromApiVersion(settingsResponse.body()!!.data.app_locations_changed)
                    .observe(
                        this, Observer {
                            l("locations updated : $it")
                            Prefs.getInstance()!!
                                .saveBackground(settingsResponse.body()!!.data.main_background)
                            Prefs.getInstance()!!
                                .saveProfileStatus(settingsResponse.body()!!.data.user_profile_status)
                            splashViewModel.saveSettings(listOf(settingsResponse.body()!!.data))
                            startActivity(
                                Intent(this@SplashActivity, MainActivity::class.java).apply {
                                    putExtra("paid_id", paidOrderId)
                                    putExtra("order_id", notifOrderId)
                                    flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                            )
                            Animatoo.animateFade(this)
                        }
                    )

            } else {
                Constants.CantConnectSnackbar(this, object : Flashbar.OnActionTapListener {
                    override fun onActionTapped(bar: Flashbar) {
                        bar.dismiss()
                        splashViewModel.getApiSettings()
                        fetchData()
                    }

                })
            }
        })
    }
}