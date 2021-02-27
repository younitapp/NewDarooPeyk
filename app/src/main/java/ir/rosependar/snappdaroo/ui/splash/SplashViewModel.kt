package ir.rosependar.snappdaroo.ui.splash

import androidx.lifecycle.*
import ir.rosependar.snappdaroo.models.ApiSettingsModel
import ir.rosependar.snappdaroo.models.ApiSettingsResponse
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.repositories.RoomRepository
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException

class SplashViewModel(val dataRepository: DataRepository, val roomRepository: RoomRepository) :
    ViewModel() {
    val splashData = MutableLiveData<Response<ApiSettingsResponse?>?>()

    init {
        getApiSettings()
    }

    fun getApiSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val res = dataRepository.getApiSettings()
                splashData.postValue(res)
            } catch (e: IOException) {
                splashData.postValue(null)
            } catch (e: Exception) {
                splashData.postValue(null)
            }
        }
    }

    fun saveSettings(settings: List<ApiSettingsModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.deleteAllSettings()
            roomRepository.insertAllSetting(settings)
        }
    }

    fun updateDataFromApiVersion(newLocDate: String): LiveData<Boolean> {
        return liveData(Dispatchers.IO) {
            supervisorScope {
                try {
                    if ((newLocDate != roomRepository.getAllSettings()[0].app_locations_changed)) {
                        val call4 = async { dataRepository.getAreaList() }
                        val areaList = call4.await().body()!!.data
                        roomRepository.deleteAllArea()
                        roomRepository.insertArea(areaList)
                        roomRepository.getAllAreas()


                        val call3 = async { dataRepository.getCountryList() }
                        val countryList = call3.await().body()!!.data!!
                        roomRepository.deleteAllCountries()
                        roomRepository.insertCountries(countryList)
                        roomRepository.getCountries()


                        val call1 = async { dataRepository.getCityList() }
                        val cityList = call1.await().body()!!.data
                        roomRepository.deleteAllCities()
                        roomRepository.insertCities(cityList)
                        roomRepository.getAllCities()

                        val call2 = async { dataRepository.getProvinceList() }
                        val provincesList = call2.await().body()!!.data
                        roomRepository.deleteAllProvinces()
                        roomRepository.insertProvinces(provincesList)
                        roomRepository.getProvinces()
                        emit(true)
                    } else {
                        emit(false)
                    }
                } catch (e: java.lang.Exception) {
                    emit(false)
                }
            }

        }

    }

}