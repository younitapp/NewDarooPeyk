package ir.rosependar.snappdaroo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ir.rosependar.snappdaroo.models.Area

@Dao
interface AreaDao {
    @Query("SELECT * FROM area")
    suspend fun getAll(): List<Area>

    @Query("SELECT * FROM area  WHERE city_id =:cityId")
    suspend fun getAreaByCityId(cityId: Long): List<Area>

    @Query("SELECT * FROM area WHERE code  =:areaIds")
    suspend fun loadAllByIds(areaIds: Int): Area

    @Query("SELECT * FROM area WHERE name LIKE :search AND city_id =:cityId")
    suspend fun searchInAreas(cityId: Long, search: String?): List<Area>
/*    @Query("SELECT * FROM city WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): City*/

    @Insert
    suspend fun insertAll(cities: List<Area>)

    @Delete
    fun delete(user: Area)

    @Query("DELETE FROM area")
    suspend fun deleteAll()
}