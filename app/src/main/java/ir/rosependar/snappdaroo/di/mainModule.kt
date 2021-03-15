package ir.rosependar.snappdaroo.di

import ir.rosependar.snappdaroo.network.ApiInterface
import ir.rosependar.snappdaroo.repositories.DataRepository
import ir.rosependar.snappdaroo.repositories.RoomRepository
import ir.rosependar.snappdaroo.ui.checkout.CheckOutViewModel
import ir.rosependar.snappdaroo.ui.contacts.ContactsViewModel
import ir.rosependar.snappdaroo.ui.home.HomeViewModel
import ir.rosependar.snappdaroo.ui.login.LoginViewModel
import ir.rosependar.snappdaroo.ui.orders.OrdersViewModel
import ir.rosependar.snappdaroo.ui.orders.orderdetail.OrderDetailViewModel
import ir.rosependar.snappdaroo.ui.profile.ProfileViewModel
import ir.rosependar.snappdaroo.ui.splash.SplashViewModel
import ir.rosependar.snappdaroo.ui.submit.SubmitOrderViewModel
import ir.rosependar.snappdaroo.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.KoinContextHandler.get
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


val mainModule = module {

    single {
        createWebService(Constants.BASE_URL)
    }
    single { RoomRepository(get()) }
    single { DataRepository(apiInterface = get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { OrdersViewModel(get(), get()) }
    viewModel { SubmitOrderViewModel(get(), get()) }
    viewModel { CheckOutViewModel(get(), get()) }
    viewModel { (orderId: String) -> OrderDetailViewModel(get(), get(), orderId) }
    viewModel { ContactsViewModel(get(), get()) }

}

private fun okHttp(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .readTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)

        .build()
}

fun createWebService(baseUrl: String): ApiInterface {
    val retrofit = Retrofit.Builder()
        .callFactory(okHttp())
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(ApiInterface::class.java)
}
