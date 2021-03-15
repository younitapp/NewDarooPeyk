package ir.rosependar.snappdaroo

import androidx.lifecycle.ViewModel
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.repositories.RoomRepository

class MainViewModel(
    private val dataRepository: DataRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

}