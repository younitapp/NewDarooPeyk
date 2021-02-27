package ir.rosependar.snappdaroo.dialogs

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.models.Area
import ir.rosependar.snappdaroo.models.City
import java.util.*


class AreaListAdapter(private var onCityClickListener: OnAreaClickListener) :
    RecyclerView.Adapter<AreaListAdapter.AreaViewHolder>() {

    private var areas =  ArrayList<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder =
        AreaViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_spinner_province_row, parent, false)
        )

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) =
        holder.bind(areas[position])

    override fun getItemCount(): Int = areas.size

    fun addAreas(areas: ArrayList<Area>) {
        this.areas.clear()
        this.areas= areas
        notifyDataSetChanged()
    }

    fun clear() {
        this.areas.clear()
        notifyDataSetChanged()
    }

    inner class AreaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tv_province: AppCompatTextView = itemView.findViewById(R.id.spinner_province)
        private var tv_firstLetter: AppCompatTextView = itemView.findViewById(R.id.firstLetter_province)

        fun bind(area: Area) {
            val province_name = area.name
            tv_province.text = province_name

            val ran = Random()
            tv_firstLetter.text = province_name.substring(0, 1)
            tv_firstLetter.background.setColorFilter(
                Color.rgb(
                    ran.nextInt(255),
                    ran.nextInt(255),
                    ran.nextInt(255)
                ), PorterDuff.Mode.OVERLAY
            )

            itemView.setOnClickListener {
                onCityClickListener.onAreaClick(area)
            }

        }
    }

    interface OnAreaClickListener {
        fun onAreaClick(area: Area)
    }

}