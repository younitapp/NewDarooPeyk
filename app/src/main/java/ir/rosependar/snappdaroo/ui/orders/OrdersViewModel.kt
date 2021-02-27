package ir.rosependar.snappdaroo.ui.orders

import androidx.lifecycle.*
import ir.rosependar.snappdaroo.models.ApiSettingsModel
import ir.rosependar.snappdaroo.models.OrderResponse
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.repositories.RoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class OrdersViewModel(
    private val dataRepository: DataRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    val settingsData = liveData(Dispatchers.IO) { emit(roomRepository.getAllSettings()) }
    fun getOrderData(): LiveData<Response<OrderResponse>?> {
        return liveData(Dispatchers.IO) {
            try {
                emit(dataRepository.getOrderList())
            } catch (e: IOException) {
                emit(null)
            }
        }
    }
}