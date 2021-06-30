package ir.rosependar.snappdaroo.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import ir.rosependar.snappdaroo.AppDatabase
import ir.rosependar.snappdaroo.models.*
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.repositories.RoomRepository
import ir.rosependar.snappdaroo.utils.Prefs
import ir.rosependar.snappdaroo.utils.l
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.io.IOException

class ProfileViewModel(
    private val dataRepository: DataRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {


    val mainLiveData = liveData(Dispatchers.IO) {
        supervisorScope {

            var cities: List<City>? = null
            var provinces: List<Province>? = null
            var countries: List<Country>? = null
            var areas: List<Area>? = null
            var profile: ProfileResponse? = null

            val call3 = async { dataRepository.getProfile() }

            areas = try {
                if (roomRepository.getAllAreas().isEmpty()) {
                    val call4 = async { dataRepository.getAreaList() }
                    val areaList = call4.await().body()!!.data!!

                    roomRepository.insertArea(areaList)
                    roomRepository.getAllAreas()
                } else {
                    roomRepository.getAllAreas()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
            countries = try {
                if (roomRepository.getCountries().isEmpty()) {
                    val call3 = async { dataRepository.getCountryList() }
                    val countryList = call3.await().body()!!.data!!
                    roomRepository.insertCountries(countryList)
                    roomRepository.getCountries()
                } else {
                    roomRepository.getCountries()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }

            cities = try {
                if (roomRepository.getAllCities().isEmpty()) {
                    val call1 = async { dataRepository.getCityList() }
                    val cityList = call1.await().body()!!.data
                    roomRepository.insertCities(cityList)
                    roomRepository.getAllCities()
                } else {
                    roomRepository.getAllCities()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
            provinces = try {
                if (roomRepository.getProvinces().isEmpty()) {
                    val call2 = async { dataRepository.getProvinceList() }
                    val provincesList = call2.await().body()!!.data
                    roomRepository.insertProvinces(provincesList)
                    roomRepository.getProvinces()
                } else {
                    roomRepository.getProvinces()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
            profile = try {
                call3.await().body()
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
            emit(AllProfileResponse(cities, provinces, areas, profile, countries))
        }

    }

    fun getCitiesByStateId(stateId: Long) = liveData(Dispatchers.IO) {
        try {
            emit(roomRepository.getCities(stateId))
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun getAreasByCityId(areaId: Long) = liveData(Dispatchers.IO) {
        try {
            emit(roomRepository.getAreas(areaId))
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun getCountries() = liveData(Dispatchers.IO) {
        try {
            emit(roomRepository.getCountries())
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun findAreaByCode(code: Int) = liveData(Dispatchers.IO) {
        try {
            emit(roomRepository.getAreaById(code))
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun findCityById(id: Int) = liveData(Dispatchers.IO) {
        try {
            emit(roomRepository.getCityById(id))
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun findProvinceById(id: Int) = liveData(Dispatchers.IO) {
        try {
            emit(roomRepository.getProvinceById(id))
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun searchInProvinces(query: String): LiveData<List<Province>?> {
        return liveData(Dispatchers.IO) {
            try {
                emit(roomRepository.searchInProvinces(query))
            } catch (e: Exception) {
                emit(null)
            }
        }
    }

    fun searchInCities(stateId: Long, query: String): LiveData<List<City>?> {
        return liveData(Dispatchers.IO) {
            try {
                emit(roomRepository.searchInCities(stateId, query))
            } catch (e: Exception) {
                emit(null)
            }
        }
    }

    fun searchInCountries(query: String): LiveData<List<Country>?> {
        return liveData(Dispatchers.IO) {
            try {
                emit(roomRepository.searchCountries(query))
            } catch (e: Exception) {
                emit(null)
            }
        }
    }

    fun searchInAreas(areaId: Long, query: String): LiveData<List<Area>?> {
        return liveData(Dispatchers.IO) {
            try {
                emit(roomRepository.searchInAreas(areaId, query))
            } catch (e: Exception) {
                emit(null)
            }
        }
    }

    fun saveProfile(
        mobile: String,
        first_name: String, last_name: String,
        country: String, provinceId: String,
        cityId: String, address: String,
        postal_code: String,
        gender: String,
        insuranceCode: String,
        birthDay: String,
        areaCode: String,
        nId: String
    ) = liveData(Dispatchers.IO) {
        try {
            emit(
                dataRepository.saveProfile(
                    first_name = first_name,
                    last_name = last_name,
                    country = country,
                    mobile = mobile,
                    provinceId = provinceId,
                    areaCode = areaCode,
                    cityId = cityId,
                    address = address,
                    postal_code = postal_code,
                    gender = gender,
                    insurance_code = insuranceCode,
                    birthday = birthDay,
                    nId = nId
                )
            )
        } catch (e: IOException) {
            emit(null)
        }

    }

    fun changeProfileStatus(status: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = roomRepository.getAllSettings()[0]
            settings.user_profile_status = status
            roomRepository.updateSetting(settings)
            l(roomRepository.getAllSettings()[0].toString())
        }
    }

}