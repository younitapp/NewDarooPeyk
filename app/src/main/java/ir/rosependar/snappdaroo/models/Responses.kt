package ir.rosependar.snappdaroo.models

import android.os.Parcelable
import android.transition.Slide
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.ArrayList

data class BaseResponse(
    val status: Int,
    val message: String
)

data class LoginModel(
    val status: Int,
    val message: String,
    val data: TokenModel
)

data class TokenModel(
    val api_token: String
)

data class MainPageResponse(

    val status: Int,
    val message: String,
    val data: MainPage
)

data class MainPage(
    val slides: MutableList<Slider>,
    val menu: MutableList<MenuItems>,
    val background: String
)

data class MenuItems(
    val name: String,
    val type: String,
    val code: String,
    val icon: String
)

data class Slider(
    val slide_image: String?,
    val target_name: String?,
    val slide_link: String?
)


data class ProvinceResponse(
    var status: Int,
    var message: String,
    var base_state: Long,
    var data: MutableList<Province>
)

@Parcelize
@Entity
data class Province(
    @PrimaryKey(autoGenerate = true) val db_id: Int?,
    var code: Long,
    var name: String
) : Parcelable

data class CityResponse(
    var status: Int,
    var message: String,
    var data: MutableList<City>
)

data class AreaResponse(
    var status: Int,
    var message: String,
    var data: MutableList<Area>
)

data class CountryResponse(
    var status: Int,
    var message: String,
    val base_country: Long?,
    var data: MutableList<Country>?
)


@Parcelize
@Entity
data class Country(
    @PrimaryKey(autoGenerate = true) val db_id: Int?,
    var code: Long,
    var name: String
) : Parcelable


@Parcelize
@Entity
data class City(
    @PrimaryKey(autoGenerate = true) val db_id: Int?,
    var code: Long,
    var name: String,
    val state_id: Long
) : Parcelable

@Parcelize
@Entity
data class Area(
    @PrimaryKey(autoGenerate = true) val db_id: Int?,
    var code: Long,
    var name: String,
    val city_id: Long
) : Parcelable


data class AllProfileResponse(
    val cities: List<City>?,
    val provinces: List<Province>?,
    val areas: List<Area>?,
    val profile: ProfileResponse?,
    val countries: List<Country>?
)


data class ProfileResponse(
    val status: Int,
    val message: String,
    val data: List<Profile?>?
)

data class Profile(
    val title: String? = null,
    val mobile: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val email: String? = null,
    val property: ProfileProperty? = null
) : Serializable

data class ProfileProperty(
    val address: String? = null,
    val postal_code: Long? = null,
    val country: String? = null,
    val birthday: String? = null,
    val city: Long? = null,
    val area: String? = null,
    val state: Long? = null,
    val insurance_code: String? = null,
    val tel: String? = null
)

data class ApiSettingsResponse(
    val status: Int,
    val message: String,
    val data: ApiSettingsModel
)

@Entity
data class ApiSettingsModel(
    @PrimaryKey(autoGenerate = true) val db_id: Int?,
    val api_version: Int,
    val app_url_ios: String,
    val app_url_android: String,
    val app_prs_delivery_time: List<CodeName>,
    val app_prs_types: List<CodeName>,
    val app_prs_statuses: List<CodeName>,
    val app_locations_changed: String,
    var user_profile_status: Int,
    val main_background: String
)

@Parcelize
data class CodeName(
    val code: Int,
    val name: String,
    val icon: String? = null
) : Parcelable

class DataConverter {

    @TypeConverter
    fun fromCodeNameList(value: List<CodeName>): String {
        val gson = Gson()
        val type = object : TypeToken<List<CodeName>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCodeNameList(value: String): List<CodeName> {
        val gson = Gson()
        val type = object : TypeToken<List<CodeName>>() {}.type
        return gson.fromJson(value, type)
    }
}

data class OrderResponse(
    val status: Int,
    val message: String,
    val data: List<Prescription>?
)

data class getOrderResponse(
    val status: Int,
    val message: String,
    val data: OrderDetail?
)

data class OrderDetail(
    val prescription: List<Prescription>?,
    val checkout: List<CheckoutModel>?
)

data class Prescription(
    val id: Int,
    val type: Int,
    val ref_type: Int,
    val status_id: Int,
    val zone_id: Int?,
    val pr_type: Int?,
    val prescription_text: String?,
    val comments: String?,
    val created_at: String,
    val images: List<OrderImage>?,
    val active: Int
)

data class CheckoutModel(
    val shipping_price: Long,
    val total_price: Long,
    val date: String,
    val comments: String?
)


data class OrderImage(
    val filename: String,
    val disk_name: String,
    val path: String,
    val extension: String
)

data class CheckOutResponse(
    val status: Int,
    val message: String,
    val data: CheckOut
)

data class CheckOut(
    val invoice_code: Long,
    val order_price: Long,
    val shipping_price: Long,
    val total_price: Long,
    val comments: String?
)

data class PaymentResponse(
    val status: Int,
    val message: String,
    val data: Payment
)

data class Payment(
    val resnum: String
)