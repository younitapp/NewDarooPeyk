package ir.rosependar.snappdaroo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.rosependar.snappdaroo.dao.*
import ir.rosependar.snappdaroo.models.*

@Database(
    entities = [City::class, Province::class, Country::class, ApiSettingsModel::class, Area::class],
    version = 2
)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun provinceDao(): ProvinceDao
    abstract fun countryDao(): CountryDao
    abstract fun areaDao(): AreaDao
    abstract fun settingDao(): SettingsDao

}