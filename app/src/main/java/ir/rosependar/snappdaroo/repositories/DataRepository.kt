package ir.rosependar.snappdaroo.repositories

import ir.rosependar.snappdaroo.models.BaseResponse
import ir.rosependar.snappdaroo.network.ApiInterface
import ir.rosependar.snappdaroo.utils.Prefs
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class DataRepository(private val apiInterface: ApiInterface) {

    suspend fun sendSms(phoneNumber: String) = apiInterface.getCode(phone_number = phoneNumber)
    suspend fun doLogin(phoneNumber: String, activateCode: String, googleToken: String) =
        apiInterface.doLogin(
            phone_number = phoneNumber,
            activate_code = activateCode,
            google_token = googleToken
        )

    suspend fun getMainPage() = apiInterface.getMainPage()
    suspend fun getProvinceList() = apiInterface.getProvinceList()
    suspend fun getCityList() = apiInterface.getCityList()
    suspend fun getAreaList() = apiInterface.getAreaList()
    suspend fun getCountryList() = apiInterface.getCountryList()
    suspend fun getProfile() = apiInterface.getProfile()
    suspend fun getOrderList() = apiInterface.getOrderList()
    suspend fun saveProfile(
        first_name: String,
        last_name: String,
        mobile: String,
        country: String,
        provinceId: String,
        cityId: String,
        address: String,
        postal_code: String,
        areaCode: String,
        gender: String,
        insurance_code: String,
        birthday: String,
        nId: String
    ): Response<BaseResponse> {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("device_id", Prefs.getInstance()!!.getDeviceId())
            .addFormDataPart("api_token", Prefs.getInstance()!!.getToken())
            .addFormDataPart("title", gender)
            .addFormDataPart("mobile", mobile)
            .addFormDataPart("first_name", first_name)
            .addFormDataPart("last_name", last_name)
            .addFormDataPart("country", country)
            .addFormDataPart("state", provinceId)
            .addFormDataPart("city", cityId)
            .addFormDataPart("area", areaCode)
            .addFormDataPart("address", address)
            .addFormDataPart("postal_code", postal_code)
            .addFormDataPart("insurance_code", insurance_code)
            .addFormDataPart("birthday", birthday)
            .addFormDataPart("nid", nId)
            .build()
        return apiInterface.saveProfile(requestBody)
    }

    suspend fun getApiSettings() = apiInterface.getApiSettings()
    suspend fun getOrder(orderId: String) = apiInterface.getOrder(orderId = orderId)
    suspend fun deleteOrder(orderId: String) = apiInterface.deleteOrder(orderId = orderId)
    suspend fun getCheckOut(orderId: String) = apiInterface.getCheckOut(orderId = orderId)
    suspend fun getRequestPayment(orderId: String) =
        apiInterface.getRequestPayment(orderId = orderId)

}