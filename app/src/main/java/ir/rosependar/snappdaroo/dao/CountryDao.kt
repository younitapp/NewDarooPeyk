package ir.rosependar.snappdaroo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ir.rosependar.snappdaroo.models.Country
import ir.rosependar.snappdaroo.models.Province
@Dao
interface CountryDao {

    @Query("SELECT * FROM country")
    suspend fun getAll(): List<Country>

    @Query("SELECT * FROM country WHERE code =:countryId")
    suspend fun loadAllByIds(countryId : Int): Country
    @Query("SELECT * FROM country WHERE name LIKE :search")
    suspend fun searchInCountries(search: String?): List<Country>
/*    @Query("SELECT * FROM city WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): City*/

    @Insert
    suspend fun insertAll( countries: List<Country>)

    @Delete
    fun delete(country : Country)

    @Query("DELETE FROM country")
    suspend fun deleteAll()
}