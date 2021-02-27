package ir.rosependar.snappdaroo.repositories

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.rosependar.snappdaroo.AppDatabase
import ir.rosependar.snappdaroo.models.*

class RoomRepository(val context: Context) {
    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "snappdaroo-db"
    ).build()


    suspend fun getAllCities() = db.cityDao().getAll()
    suspend fun getAllAreas() = db.areaDao().getAll()
    suspend fun getCities(stateId: Long) = db.cityDao().getCityByStateId(stateId)
    suspend fun getAreas(cityId: Long) = db.areaDao().getAreaByCityId(cityId)
    suspend fun getCityById(AreaIdies: Int) = db.cityDao().loadAllByIds(AreaIdies)
    suspend fun getAreaById(AreaIdies: Int) = db.areaDao().loadAllByIds(AreaIdies)
    suspend fun getProvinceById(city_idies: Int) = db.provinceDao().loadAllByIds(city_idies)
    suspend fun insertCities(cities: List<City>) = db.cityDao().insertAll(cities)
    suspend fun deleteAllCities() = db.cityDao().deleteAll()


    suspend fun insertArea(areas: List<Area>) = db.areaDao().insertAll(areas)


    suspend fun deleteAllArea() = db.areaDao().deleteAll()

    suspend fun getProvinces() = db.provinceDao().getAll()
    suspend fun getCountries() = db.countryDao().getAll()
    suspend fun insertProvinces(provinces: List<Province>) = db.provinceDao().insertAll(provinces)
    suspend fun deleteAllProvinces() = db.provinceDao().deleteAll()

    suspend fun insertCountries(countries: List<Country>) = db.countryDao().insertAll(countries)
    suspend fun deleteAllCountries() = db.countryDao().deleteAll()

    suspend fun searchInProvinces(query: String) = db.provinceDao().searchInProvinces(query)
    suspend fun searchInCities(stateId: Long, query: String) =
        db.cityDao().searchInCities(stateId, query)

    suspend fun searchCountries(query: String) = db.countryDao().searchInCountries(query)
    suspend fun searchInAreas(cityId: Long, query: String) =
        db.areaDao().searchInAreas(cityId, query)

    suspend fun deleteAllSettings() = db.settingDao().deleteAll()
    suspend fun getAllSettings() = db.settingDao().getAll()
    suspend fun insertAllSetting(settings: List<ApiSettingsModel>) =
        db.settingDao().insertAll(settings)

    suspend fun updateSetting(settings: ApiSettingsModel) = db.settingDao().update(settings)

}