package ir.rosependar.snappdaroo.ui.bonus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ir.rosependar.snappdaroo.repositories.DataRepository
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class BonusViewModel(
    private val dataRepository: DataRepository,
) : ViewModel() {

    val bonusCode = MutableLiveData("")

    val bonusResponse = liveData(Dispatchers.IO) {
        try {
            emit(bonusCode.value?.let { dataRepository.sendBonusCode(it) })
        } catch (e: IOException) {
            emit(null)
        }
    }

}