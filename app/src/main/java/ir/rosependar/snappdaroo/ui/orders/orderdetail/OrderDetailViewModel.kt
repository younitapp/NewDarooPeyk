package ir.rosependar.snappdaroo.ui.orders.orderdetail

import androidx.lifecycle.*
import ir.rosependar.snappdaroo.models.ApiSettingsModel
import ir.rosependar.snappdaroo.models.BaseResponse
import ir.rosependar.snappdaroo.models.CheckOutResponse
import ir.rosependar.snappdaroo.models.getOrderResponse
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.repositories.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class OrderDetailViewModel(
    private val dataRepository: DataRepository,
    private val roomRepository: RoomRepository,
    private val orderId: String
) : ViewModel() {

    val orderData = MutableLiveData<Response<getOrderResponse>?>()
    val settingsData = MutableLiveData<List<ApiSettingsModel>>()
    val responseDeleteOrder = MutableLiveData<Response<BaseResponse>>()

    init {
        getOrder(orderId)
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                responseDeleteOrder.postValue(dataRepository.deleteOrder(orderId))
            } catch (e: IOException) {
                responseDeleteOrder.postValue(null)
            }
        }
    }

    private fun getOrder(orderId: String) {

        viewModelScope.launch(Dispatchers.IO) {
            settingsData.postValue(roomRepository.getAllSettings())
            try {
                orderData.postValue(dataRepository.getOrder(orderId))
            } catch (e: IOException) {
                orderData.postValue(null)
            }
        }
    }

    fun getCheckOut(orderId: String): LiveData<Response<CheckOutResponse>?> {
        return liveData(Dispatchers.IO) {
            try {
                emit(dataRepository.getCheckOut(orderId))
            } catch (e: IOException) {
                emit(null)
            }
        }


    }
}