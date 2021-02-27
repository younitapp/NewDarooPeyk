package ir.rosependar.snappdaroo.utils


import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

import androidx.core.internal.view.SupportMenu
import ir.rosependar.snappdaroo.MyApplication
import ir.rosependar.snappdaroo.R
import java.lang.reflect.Field

const val TAG: String = "SnappDaroo"
var WIDTH = -1
fun l(message: String) {
    Log.e(TAG, message)
}

fun successToast(msg : String) {
    customToast(msg, getColor(R.color.bg_toast_success))
}

fun errorToast(msg : String) {
    customToast(msg, getColor(R.color.bg_toast_error))
}
fun getColor(@ColorRes resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= 23)
        ContextCompat.getColor(MyApplication.instance!!, resId)
    else
        MyApplication.instance!!.resources.getColor(resId)
}
private fun customToast(msg: String, backgroundColor: Int) {
    val layout = LinearLayout(MyApplication.instance)
    layout.setBackgroundColor(backgroundColor)
    val tv = TextView(MyApplication.instance)
    tv.setTextColor(SupportMenu.CATEGORY_MASK)
    tv.textSize = 13f
    tv.gravity = 16
    tv.text = msg
    tv.gravity = 17
    tv.maxWidth = screenWidth() - 110
    tv.setTextColor(Color.WHITE)
    tv.typeface = FontsOverride.font
    layout.addView(tv)
    layout.setPadding(35, 25, 35, 25)
    val gradientDrawable = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(backgroundColor, backgroundColor)
    )
    gradientDrawable.cornerRadius = 16f
    if (Build.VERSION.SDK_INT >= 17) {
        layout.background = gradientDrawable
    } else {
        layout.setBackgroundDrawable(gradientDrawable)
    }
    val toast = Toast(MyApplication.instance)
    toast.view = layout
    toast.setGravity(80, 0, 100)
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
}
fun screenWidth(): Int {
    if (WIDTH == -1) {
        WIDTH = MyApplication.instance?.resources?.displayMetrics!!.widthPixels
    }
    return WIDTH
}
object FontsOverride {

    lateinit var font: Typeface

    fun setDefaultFont(fontAssetName: Typeface?) {
        if (fontAssetName != null) {
            font = fontAssetName
            replaceFont()
        }
    }

    private fun replaceFont() {
        try {
            val staticField: Field = Typeface::class.java.getDeclaredField("MONOSPACE")
            staticField.isAccessible = true
            staticField.set(null, font)
        } catch (e: Exception) {
            l(e.toString())
        }

    }
}
