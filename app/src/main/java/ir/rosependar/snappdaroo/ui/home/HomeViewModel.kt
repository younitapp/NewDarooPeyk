package ir.rosependar.snappdaroo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ir.rosependar.snappdaroo.models.Slider
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.repositories.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class HomeViewModel(
    private val dataRepository: DataRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    val sliderResponse = liveData(Dispatchers.IO) {
        try {
            emit(dataRepository.getMainPage())
        } catch (e: IOException) {
            emit(null)
        }
    }
    val HomeResponse = liveData(Dispatchers.IO) {

        emit(roomRepository.getAllSettings())

    }

    fun getProfileStatus() : LiveData<Int> {
        return liveData(Dispatchers.IO) {
            val statusid =  roomRepository.getAllSettings()[0]
            emit(statusid.user_profile_status)
        }
    }
}

