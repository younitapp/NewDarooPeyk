package ir.rosependar.snappdaroo.utils

import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import ir.rosependar.snappdaroo.R
import kotlinx.android.synthetic.main.custom_progress_dialog.view.*

class CustomProgressDialog {

    lateinit var dialog: Dialog

    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun show(context: Context, title:CharSequence?): Dialog {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.custom_progress_dialog, null)
        if (title != null) {
            view.cp_title.text = title
        }
        view.cp_bg_view.setBackgroundColor(Color.parseColor("#60000000")) //Background Color
        view.cp_cardview.setCardBackgroundColor(Color.parseColor("#70000000")) //Box Color
        setColorFilter(view.cp_pbar.indeterminateDrawable,
            ResourcesCompat.getColor(context.resources, R.color.yellowColor, null)) //Progress Bar Color
        view.cp_title.setTextColor(Color.WHITE) //Text Color

        dialog = Dialog(context, R.style.CustomProgressBarTheme)
        dialog.setContentView(view)
        dialog.show()

        return dialog
    }

    fun setColorFilter(@NonNull drawable: Drawable, color:Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}