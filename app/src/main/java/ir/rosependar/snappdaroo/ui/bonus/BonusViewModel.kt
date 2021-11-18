package ir.rosependar.snappdaroo.ui.bonus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ir.rosependar.snappdaroo.models.BaseResponse
import ir.rosependar.snappdaroo.repositories.DataRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.io.IOException

class BonusViewModel(
    private val dataRepository: DataRepository,
) : ViewModel() {

    val bonusCode = MutableLiveData("")

    fun bonusResponse(bonusCode: String): LiveData<Response<BaseResponse>?> {
        return liveData(Dispatchers.IO) {
            try {
                emit(dataRepository.sendBonusCode(bonusCode))
            } catch (e: IOException) {
                emit(null)
            }
        }
    }

}