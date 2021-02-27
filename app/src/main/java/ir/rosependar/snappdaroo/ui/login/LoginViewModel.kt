package ir.rosependar.snappdaroo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ir.rosependar.snappdaroo.models.BaseResponse
import ir.rosependar.snappdaroo.models.LoginModel
import ir.rosependar.snappdaroo.repositories.DataRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.io.IOException

class LoginViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getSmsCode(phoneNumber : String): LiveData<Response<BaseResponse>?> {
        return liveData(Dispatchers.IO){
            try {
                emit(dataRepository.sendSms(phoneNumber))
            }catch (e : IOException){
                emit(null)
            }
        }
    }
    fun doLogin(phoneNumber: String , activateCode: String,googleToken : String): LiveData<Response<LoginModel>?> {
        return liveData(Dispatchers.IO) {
            try {
                emit(dataRepository.doLogin(phoneNumber , activateCode,googleToken))
            }catch (e : IOException){
                emit(null)
            }
        }
    }


}