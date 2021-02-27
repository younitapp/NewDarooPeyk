package ir.rosependar.snappdaroo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import ir.rosependar.snappdaroo.di.mainModule
import ir.rosependar.snappdaroo.utils.FontsOverride
import net.gotev.uploadservice.UploadServiceConfig
import net.gotev.uploadservice.data.UploadNotificationConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application() {
    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
        const val notificationChannelID = "SnappDarooChannel"
    }

    // Customize the notification channel as you wish. This is only for a bare minimum example
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                notificationChannelID,
                "SnappDaroo Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        UploadServiceConfig.initialize(
            context = this,
            defaultNotificationChannel = notificationChannelID,
            debug = BuildConfig.DEBUG
        )

        /*UploadServiceConfig.notificationHandlerFactory = { service ->
            MyNotificationHandler(service)
        }*/
        instance = this
        FontsOverride.setDefaultFont(ResourcesCompat.getFont(this, R.font.iran_sans_light))
        // start Koin!
        startKoin {
            // Android context
            androidContext(this@MyApplication)
            // modules
            modules(mainModule)
        }

    }
}