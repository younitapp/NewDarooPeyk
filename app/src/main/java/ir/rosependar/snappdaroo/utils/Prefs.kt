package ir.rosependar.snappdaroo.utils

import android.content.Context
import android.content.SharedPreferences
import ir.rosependar.snappdaroo.MyApplication

class Prefs private constructor() {

    companion object {

        @get:Synchronized var prefs: Prefs? = null
            private set

        fun getInstance(): Prefs? {
            if (prefs == null) {
                prefs = Prefs()
            }
            return prefs!!
        }

        @get:Synchronized var sharedPreferences: SharedPreferences? = null
            private set

        private fun get(): SharedPreferences {
            if (sharedPreferences == null) {
                sharedPreferences = MyApplication.instance!!.getSharedPreferences(
                    SP_NAME, Context.MODE_PRIVATE
                )
            }
            return sharedPreferences!!
        }

        private const val SP_NAME          = "Snappdaroo"
        const val TOKEN                    = "TOKEN"
        const val DEVICE_ID                = "DEVICE_ID"
        const val BASE_PROVINCE            = "BASE_PROVINCE"
        const val CATEGORY_VERSION         = "CATEGORY_VERSION"
        const val FEATURED_PRODUCT_VERSION = "FEATURED_PRODUCT_VERSION"
        const val SHIPPING_ACTIVE          = "SHIPPING_ACTIVE"
        const val MIN_ORDER                = "MIN_ORDER"
        const val VENDOR_NUMBER                = "VENDOR_NUMBER"
        const val VENDOR_WEBPAGE                = "VENDOR_WEBPAGE"
        const val MAINPAGE_CATEGORIES                = "MAINPAGE_CATEGORIES"
        const val BACKGROUND_IMAGE                = "BACKGROUND_IMAGE"
        const val PROFILE_STATUS                = "PROFILE_STATUS"

    }
    fun saveProfileStatus(status: Int) {
        get().edit().putInt(PROFILE_STATUS, status).commit()
    }

    fun getProfileStatus(): Int {
        return get().getInt(PROFILE_STATUS,0)
    }
    fun saveBackground(background: String) {
        get().edit().putString(BACKGROUND_IMAGE, background).apply()
    }

    fun getBackground(): String {
        return get().getString(BACKGROUND_IMAGE, "")!!
    }

    fun saveToken(token: String) {
        get().edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String {
        return get().getString(TOKEN, "")!!
    }
    fun saveVendorNumber(number: String) {
        get().edit().putString(VENDOR_NUMBER, number).apply()
    }

    fun getVendorNumber(): String {
        return get().getString(VENDOR_NUMBER, "")!!
    }

    fun saveVendorWebPage(web_page: String) {
        get().edit().putString(VENDOR_WEBPAGE, web_page).apply()
    }

    fun getVendorWebPage(): String {
        return get().getString(VENDOR_WEBPAGE, "")!!
    }

    fun saveMainPageCategories(categories: String) {
        get().edit().putString(MAINPAGE_CATEGORIES, categories).apply()
    }

    fun getMainPageCategories(): String {
        return get().getString(MAINPAGE_CATEGORIES, "")!!
    }



    fun saveDeviceId(deviceId: String) {
        get().edit().putString(DEVICE_ID, deviceId).apply()
    }

    fun getDeviceId(): String {
        return get().getString(DEVICE_ID, "")!!
    }

    fun saveBaseProvince(baseState: Long) {
        get().edit().putLong(BASE_PROVINCE, baseState).apply()
    }

    fun getBaseProvince(): Long {
        return get().getLong(BASE_PROVINCE, 0)
    }

    fun saveCategoryVersion(categoryVersion: Long) {
        get().edit().putLong(CATEGORY_VERSION, categoryVersion).apply()
    }

    fun getCategoryVersion(): Long {
        return get().getLong(CATEGORY_VERSION, (-1).toLong())
    }

    fun saveFeaturedProductVersion(featuredProduct: Long) {
        get().edit().putLong(FEATURED_PRODUCT_VERSION, featuredProduct).apply()
    }

    fun getFeaturedProductVersion(): Long {
        return get().getLong(FEATURED_PRODUCT_VERSION, (-1).toLong())
    }

    fun saveShippingActive(shippingActive: Int) {
        get().edit().putInt(SHIPPING_ACTIVE, shippingActive).apply()
    }

    fun getShippingActive(): Int {
        return get().getInt(SHIPPING_ACTIVE, -1)
    }

    fun saveMinOrder(minOrder: Int) {
        get().edit().putInt(MIN_ORDER, minOrder).apply()
    }

    fun getMinOrder(): Int {
        return get().getInt(MIN_ORDER, -1)
    }

    fun existKey(key: String): Boolean {
        return get().contains(key)
    }

    fun clearKey(key: String) {
        get().edit().remove(key).apply()
    }

}