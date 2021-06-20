package ir.rosependar.snappdaroo.utils

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import ir.rosependar.snappdaroo.R
import java.io.File


class Constants {
    companion object {
        val BASE_URL = "https://www.daroopeyk.com/api/v1/"
        val SITE_URL = "https://www.daroopeyk.com/"
        val FILE_URL = "https://www.daroopeyk.com/storage/app/media/"
        const val API_VERSION = 1
        suspend fun compressImage(context: Context, imageFile: File): File {
            return Compressor.compress(context, imageFile) {
                default(
                    width = 800,
                    height = 400,
                    quality = 20,
                    format = Bitmap.CompressFormat.JPEG
                )
            }
        }

        fun CantConnectSnackbar(activity: Activity, actionListener: Flashbar.OnActionTapListener) {
            Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.BOTTOM)
                .title("مشکل در اتصال به سرور")
                .message("متاسفانه مشکلی در اتصال شما به برنامه بوجود آمده است.")
                .backgroundColorRes(R.color.title_blue)
                .primaryActionText("اتصال دوباره")
                .primaryActionTextColor(R.color.bg_price_discount)
                .enterAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot()
                )
                .exitAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate()
                )
                .titleTypeface(Typeface.createFromAsset(activity.assets, "iran_sans_bold.ttf"))
                .messageTypeface(Typeface.createFromAsset(activity.assets, "iran_sans.ttf"))
                .primaryActionTextTypeface(
                    Typeface.createFromAsset(
                        activity.assets,
                        "iran_sans_bold.ttf"
                    )
                )
                .primaryActionTapListener(actionListener)
                .build()
                .show()
        }

        fun getRealPathFromURI(mContext: Context, contentUri: Uri): String? {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            val loader = CursorLoader(mContext, contentUri, proj, null, null, null)
            val cursor: Cursor = loader.loadInBackground()!!
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val result: String = cursor.getString(column_index)
            cursor.close()
            return result
        }

    }
}