package ir.rosependar.snappdaroo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ir.rosependar.snappdaroo.models.City
import ir.rosependar.snappdaroo.models.Province

@Dao
interface CityDao {
    @Query("SELECT * FROM city")
    suspend fun getAll(): List<City>

    @Query("SELECT * FROM city  WHERE state_id =:stateId")
    suspend fun getCityByStateId(stateId: Long): List<City>

    @Query("SELECT * FROM city WHERE code  =:cityIds")
    suspend fun loadAllByIds(cityIds: Int): City

    @Query("SELECT * FROM city WHERE name LIKE :search AND state_id =:stateId")
    suspend fun searchInCities(stateId: Long, search: String?): List<City>
/*    @Query("SELECT * FROM city WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): City*/

    @Insert
    suspend fun insertAll(cities: List<City>)

    @Delete
    fun delete(user: City)

    @Query("DELETE FROM city")
    suspend fun deleteAll()
}