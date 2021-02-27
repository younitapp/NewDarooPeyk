package ir.rosependar.snappdaroo.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ir.rosependar.snappdaroo.models.CheckOutResponse
import ir.rosependar.snappdaroo.models.PaymentResponse
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.repositories.RoomRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.io.IOException

class CheckOutViewModel(private val dataRepository: DataRepository , private val roomRepository: RoomRepository) : ViewModel() {
    fun getCheckOutFactor(prescriptionId : String): LiveData<Response<CheckOutResponse>?> {
        return liveData(Dispatchers.IO){
            try {
                emit(dataRepository.getCheckOut(prescriptionId))
            }catch (e : IOException){
                emit(null)
            }
        }
    }
    fun getRequestPayment(prescriptionId: String): LiveData<Response<PaymentResponse>?> {
        return liveData(Dispatchers.IO) {
            try {
                emit(dataRepository.getRequestPayment(prescriptionId))
            }catch (e : IOException){
                emit(null)
            }
        }
    }
}