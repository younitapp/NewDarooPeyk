package ir.rosependar.snappdaroo.utils

import android.graphics.Paint
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import java.text.DecimalFormat

object PriceUtil {

    fun getThousandSeparator(): DecimalFormat {
        return DecimalFormat("#,###.##")
    }




}