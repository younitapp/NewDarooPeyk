package ir.rosependar.snappdaroo.ui.login

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.broadcastrecievers.MySMSBroadcastReceiver
import ir.rosependar.snappdaroo.utils.l

class LoginActivity : AppCompatActivity() {
    var smsReceiver: MySMSBroadcastReceiver? = null
    var googleToken = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        startSMSListener()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                googleToken = if (task.result?.token != null) task.result?.token!! else ""
            })
    }

    private fun startSMSListener() {
        try {
            smsReceiver = MySMSBroadcastReceiver()


            val intentFilter = IntentFilter()
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
            registerReceiver(smsReceiver, intentFilter)

            val client = SmsRetriever.getClient(this)

            val task = client.startSmsRetriever()
            task.addOnSuccessListener {
                l("SMS Retriever Started.")
            }

            task.addOnFailureListener {
                l("SMS Retriever Failed to start :  ${it.localizedMessage}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}