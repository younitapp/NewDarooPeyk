package ir.rosependar.snappdaroo.ui.home

import android.app.Activity
import android.text.Layout
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.rosependar.snappdaroo.MainActivity
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.models.CodeName
import ir.rosependar.snappdaroo.models.MenuItems
import ir.rosependar.snappdaroo.utils.Constants
import ir.rosependar.snappdaroo.utils.Constants.Companion.FILE_URL
import ir.rosependar.snappdaroo.utils.l
import kotlinx.android.synthetic.main.fragment_time_picker_list_dialog_item.view.*

class ItemTypeAdapter(
    val listener: ItemClickListener,
    val itemTypes: List<MenuItems>,
    val activityRefrence: Activity
) : RecyclerView.Adapter<ItemTypeAdapter.myViewHolder>() {
    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ic_drug = itemView.findViewById<ImageView>(R.id.ic_drug)
        val txt_type = itemView.findViewById<TextView>(R.id.txt_type)
        val crd_main = itemView.findViewById<CardView>(R.id.crd_main)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false)
        )
    }

    override fun getItemCount(): Int = itemTypes.size

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val type = itemTypes[position]
        if (type.type == "divider") {
            holder.itemView.visibility = View.GONE
        }
        val mainActivity = activityRefrence as MainActivity
        l(type.icon.toString())
        Glide.with(holder.ic_drug).load(FILE_URL + type.icon).into(holder.ic_drug)
        holder.txt_type.text = type.name

        holder.crd_main.setOnClickListener {
            if (type.type == "prescription") {
                listener.OnTypeClicked(type.code.toInt())
            }

            if (type.type == "link") {
                listener.OnLinkClicked(type.code)
            }
        }
    }

}

interface ItemClickListener {
    fun OnTypeClicked(code: Int)
    fun OnLinkClicked(link: String)
}