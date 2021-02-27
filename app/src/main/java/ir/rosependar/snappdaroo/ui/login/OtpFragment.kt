package ir.rosependar.snappdaroo.ui.login

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import ir.rosependar.snappdaroo.MainActivity
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.broadcastrecievers.MySMSBroadcastReceiver
import ir.rosependar.snappdaroo.ui.splash.SplashActivity
import ir.rosependar.snappdaroo.utils.*
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_otp.textView2
import kotlinx.android.synthetic.main.login_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class OtpFragment : Fragment(), MySMSBroadcastReceiver.OTPReceiveListener {
    private val loginViewModel: LoginViewModel by viewModel()
    val phoneNumber by lazy { requireArguments().getString("phone_number", "") }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_otp, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as LoginActivity).smsReceiver?.initOTPListener(this)



        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }
        btn_resend.setOnClickListener {
            val customProgressDialog = CustomProgressDialog().let {

                it.show(requireContext(), "لطفا صبر کنید.")

            }
            customProgressDialog.setCancelable(false)
            loginViewModel.getSmsCode("+${phoneNumber}").observe(viewLifecycleOwner, Observer {
                customProgressDialog.dismiss()
                if (it != null && it.isSuccessful) {
                    btn_resend.visibility = View.GONE
                    countdown.visibility = View.VISIBLE
                    countdown.start(120000)
                    countdown.setOnCountdownEndListener {
                        btn_resend.visibility = View.VISIBLE
                        countdown.visibility = View.GONE
                    }
                } else {
                    errorToast("مشکلی در ارسال اس ام اس بوجود آمده است.")
                }
            })

        }
        textView2.text = "کد به شماره ی $phoneNumber ارسال شد."
        otp_view.setOtpCompletionListener {
            registerUser()
        }
        btn_verify_sms.setOnClickListener {
            registerUser()
        }
    }


    fun registerUser() {
        val customProgressDialog = CustomProgressDialog().let {
            it.show(requireContext(), "لطفا صبر کنید.")
        }
        customProgressDialog.setCancelable(false)
        val googleToken = try {
            (activity as LoginActivity).googleToken
        } catch (e: Exception) {
            ""
        }
        loginViewModel.doLogin("+${phoneNumber}", otp_view.text.toString(), googleToken)
            .observe(viewLifecycleOwner,
                Observer {
                    customProgressDialog.dismiss()
                    if (it != null && it.isSuccessful) {
                        when (it.body()!!.status) {
                            1 -> {
                                if (it.body() != null) {
                                    Prefs.getInstance()!!.saveToken(it.body()!!.data.api_token)
                                    Intent(
                                        activity as LoginActivity,
                                        SplashActivity::class.java
                                    ).apply {
                                        flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(this)
                                    }
                                } else {
                                    errorToast("متاسفانه مشکلی در ورود شما پیش آمده است.")
                                }
                            }
                            -1 -> {
                                errorToast("کد وارد شده ی شما صحیح نمیباشد.")
                            }
                        }
                    } else {
                        errorToast("متاسفانه مشکلی در تایید کد بوجود آمده است. لطفا دوباره امتحان کنید.")
                    }
                })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            OtpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onOTPReceived(otp: String) {
        otp_view.setText(otp)
    }

    override fun onOTPTimeOut() {
        l("OTP Timed Out")
    }
}