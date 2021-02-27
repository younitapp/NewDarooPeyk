package ir.rosependar.snappdaroo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ir.rosependar.snappdaroo.models.City
import ir.rosependar.snappdaroo.models.Province

@Dao
interface ProvinceDao {
    @Query("SELECT * FROM province")
    suspend fun getAll(): List<Province>

    @Query("SELECT * FROM province WHERE code =:provinceIdies")
    suspend fun loadAllByIds(provinceIdies : Int): Province
    @Query("SELECT * FROM province WHERE name LIKE :search")
    suspend fun searchInProvinces(search: String?): List<Province>
/*    @Query("SELECT * FROM city WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): City*/

    @Insert
    suspend fun insertAll( provinces: List<Province>)

    @Delete
    fun delete(province : Province)

    @Query("DELETE FROM province")
    suspend fun deleteAll()
}