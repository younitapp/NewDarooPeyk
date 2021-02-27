package ir.rosependar.snappdaroo.dao

import androidx.room.*
import ir.rosependar.snappdaroo.models.ApiSettingsModel

@Dao
interface SettingsDao {
    @Query("SELECT * FROM ApiSettingsModel")
    suspend fun getAll(): List<ApiSettingsModel>

    @Query("DELETE FROM ApiSettingsModel")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll( settings: List<ApiSettingsModel>)


    @Update(entity = ApiSettingsModel::class)
    suspend fun update(obj : ApiSettingsModel)

}