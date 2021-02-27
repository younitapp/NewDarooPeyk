package ir.rosependar.snappdaroo.dialogs


import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.models.Province
import java.util.*

class ProvinceListAdapter(private var onProvinceClickListener: ProvinceDialogFragment) :
    RecyclerView.Adapter<ProvinceListAdapter.ProvinceViewHolder>() {

    private var provinces =  ArrayList<Province>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceViewHolder =
        ProvinceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_spinner_province_row, parent, false)
        )

    override fun onBindViewHolder(holder: ProvinceViewHolder, position: Int) =
        holder.bind(provinces[position])

    override fun getItemCount(): Int = provinces.size

    fun addProvinces(provinces: ArrayList<Province>) {
        this.provinces.clear()
        this.provinces= provinces
        notifyDataSetChanged()
    }

    fun clear() {
        this.provinces.clear()
        notifyDataSetChanged()
    }

    inner class ProvinceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tv_province: AppCompatTextView = itemView.findViewById(R.id.spinner_province)
        private var tv_firstLetter: AppCompatTextView = itemView.findViewById(R.id.firstLetter_province)

        fun bind(province: Province) {
            val province_name = province.name
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
                onProvinceClickListener?.onProvinceClick(province)
            }

        }
    }

    interface OnProvinceClickListener {
        fun onProvinceClick(province: Province)
    }

}