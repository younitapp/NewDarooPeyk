package ir.rosependar.snappdaroo.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.utils.CustomProgressDialog
import ir.rosependar.snappdaroo.utils.Prefs
import ir.rosependar.snappdaroo.utils.errorToast
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.compat.ViewModelCompat.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }


    private val loginViewModel : LoginViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        saveAndroidId()
        ccp.registerCarrierNumberEditText(editText_carrierNumber)
        btn_send_sms.setOnClickListener {
            if(!ccp.isValidFullNumber) {
                errorToast("لطفا از صحت شماره تلفن خود مطمئن شوید.")
            }else{
                RulesDialog.listener = object : RulesDialog.RulesListener{
                    override fun onRulesAccepted() {
                        sendSms()
                    }
                }
              RulesDialog.newInstance().show(parentFragmentManager,"")
            }
        }

    }
    fun sendSms(){
        val customProgressDialog = CustomProgressDialog().let {

            it.show(requireContext(),"لطفا صبر کنید.")

        }
        customProgressDialog.setCancelable(false)
        loginViewModel.getSmsCode(ccp.fullNumber).observe(viewLifecycleOwner, Observer {
            customProgressDialog.dismiss()
            if( it != null && it.isSuccessful) {
                val body = it.body()!!
                if(body.status == 1 || body.status == 2){

                    findNavController().navigate(R.id.action_loginFragment_to_otpFragment,Bundle().apply {
                        putString("phone_number" ,ccp.fullNumber )
                    })
                }
            }else{
                errorToast("متاسافنه مشکلی در دریافت اس ام اس بوجود آمده است ، لطفا بعدا امتحان کنید.")
            }
        })
    }
    private fun saveAndroidId(){
        val androidId = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        Prefs.getInstance()!!.saveDeviceId(androidId)
    }

}