package ir.rosependar.snappdaroo.ui.orders

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ir.rosependar.snappdaroo.MainActivity
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.models.CodeName
import ir.rosependar.snappdaroo.models.Prescription
import ir.rosependar.snappdaroo.utils.l
import saman.zamani.persiandate.PersianDate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(
    val listener: orderClickListener,
    private val orderList: List<Prescription>,
    private val types: List<CodeName>,
    private val statuses: List<CodeName>
) : RecyclerView.Adapter<OrderAdapter.myViewHolder>() {

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_orderNumber = itemView.findViewById<TextView>(R.id.order_history_orderNumber)
        val txt_date = itemView.findViewById<TextView>(R.id.order_history_createdAt)
        val txt_type = itemView.findViewById<TextView>(R.id.order_history_productName)
        val txt_status = itemView.findViewById<TextView>(R.id.order_history_status)
        val lyt_main = itemView.findViewById<LinearLayout>(R.id.lyt_main)
        val img_status = itemView.findViewById<View>(R.id.img_status)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        )
    }

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val order = orderList[position]
        holder.txt_orderNumber.text = "شماره درخواست : ${order.id}"
        holder.txt_date.text = order.created_at
        val typeText = types.find {
            it.code == order.type
        }
        holder.txt_type.text = typeText?.name
        val status = statuses.find {
            it.code == order.status_id
        }
        holder.txt_status.text = status?.name
        var color: Int = 0
        when (status?.code) {
            1 -> {
                color = Color.parseColor("#A9A9A9")
            }
            2 -> {
                color = Color.parseColor("#007CFF")
            }
            3 -> {
                color = Color.parseColor("#FFAA00")

            }
            4 -> {
                color = Color.parseColor("#00FFA6")
            }
            5 -> {
                color = Color.parseColor("#003EFF")
            }
            6 -> {
                color = Color.parseColor("#FF003A")
            }
            7 -> {
                color = Color.parseColor("#8700FF")
            }
            8 -> {
                color = Color.parseColor("#02B4A9")
            }
            9 -> {
                color = Color.parseColor("#C69300")
            }
        }
        l("milOrder $order")
        holder.txt_status.setTextColor(color)
        holder.img_status.backgroundTintList = ColorStateList.valueOf(color)
        holder.lyt_main.setOnClickListener {
            listener.OnOrderClicked(
                order.id, types

            )
        }
    }

}

interface orderClickListener {
    fun OnOrderClicked(id: Int, types: List<CodeName>)
}