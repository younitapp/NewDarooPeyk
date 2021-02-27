package ir.rosependar.snappdaroo.ui.submit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.rosependar.snappdaroo.models.ApiSettingsModel
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.repositories.RoomRepository
import kotlinx.coroutines.launch

class SubmitOrderViewModel(private val dataRepository: DataRepository , private val roomRepository: RoomRepository) : ViewModel() {
    val settingsData = MutableLiveData<ApiSettingsModel>()
    init {
        getSettings()
    }
    fun getSettings(){
        viewModelScope.launch {
            settingsData.postValue(roomRepository.getAllSettings()[0])
        }
    }

}