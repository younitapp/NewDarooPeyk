package ir.rosependar.snappdaroo.network

import ir.rosependar.snappdaroo.models.*
import ir.rosependar.snappdaroo.utils.Prefs
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST("rpsapi_set_bonus")
    suspend fun sendBonusCode(
        @Header("deviceid") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Header("apitoken") api_token: String = Prefs.getInstance()!!.getToken(),
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @GET("rpsapi_remove_order")
    suspend fun deleteOrder(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("api_token") api_token: String = Prefs.getInstance()!!.getToken(),
        @Query("prescription_id") orderId: String
    ): Response<BaseResponse>

    @GET("rpsapi_get_countries")
    suspend fun getCountryList(
    ): Response<CountryResponse>

    @GET("rpsapi_get_states")
    suspend fun getProvinceList(
    ): Response<ProvinceResponse>

    @GET("rpsapi_get_cities")
    suspend fun getCityList(
    ): Response<CityResponse>


    @GET("rpsapi_get_areas")
    suspend fun getAreaList(
    ): Response<AreaResponse>

    @GET("rpsapi_get_sms")
    suspend fun getCode(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("line_number") phone_number: String
    ): Response<BaseResponse>

    @GET("rpsapi_register")
    suspend fun doLogin(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("line_number") phone_number: String,
        @Query("activate_code") activate_code: String,
        @Query("google_token") google_token: String = ""
    ): Response<LoginModel>

    @GET("rpsapi_get_main_page")
    suspend fun getMainPage(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("api_token") api_token: String = Prefs.getInstance()!!.getToken()
    ): Response<MainPageResponse>

    @GET("rpsapi_get_profile")
    suspend fun getProfile(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("api_token") api_token: String = Prefs.getInstance()!!.getToken()
    ): Response<ProfileResponse>

    @POST("rpsapi_save_profile")
    suspend fun saveProfile(
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @GET("rpsapi_get_api_settings")
    suspend fun getApiSettings(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("api_token") api_token: String = Prefs.getInstance()!!.getToken()
    ): Response<ApiSettingsResponse?>

    @GET("rpsapi_order_list")
    suspend fun getOrderList(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("api_token") api_token: String = Prefs.getInstance()!!.getToken()
    ): Response<OrderResponse>

    @GET("rpsapi_get_order")
    suspend fun getOrder(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("api_token") api_token: String = Prefs.getInstance()!!.getToken(),
        @Query("prescription_id") orderId: String
    ): Response<getOrderResponse>

    @GET("rpsapi_get_checkout")
    suspend fun getCheckOut(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("api_token") api_token: String = Prefs.getInstance()!!.getToken(),
        @Query("prescription_id") orderId: String
    ): Response<CheckOutResponse>

    @GET("rpsapi_payment_request")
    suspend fun getRequestPayment(
        @Query("device_id") device_id: String = Prefs.getInstance()!!.getDeviceId(),
        @Query("api_token") api_token: String = Prefs.getInstance()!!.getToken(),
        @Query("prescription_id") orderId: String
    ): Response<PaymentResponse>
}