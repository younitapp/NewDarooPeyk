package ir.rosependar.snappdaroo.dialogs


import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.models.City
import ir.rosependar.snappdaroo.models.Province
import java.util.*

class CityListAdapter(private var onCityClickListener: OnCityClickListener) :
    RecyclerView.Adapter<CityListAdapter.CityViewHolder>() {

    private var cities =  ArrayList<City>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder =
        CityViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_spinner_province_row, parent, false)
        )

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) =
        holder.bind(cities[position])

    override fun getItemCount(): Int = cities.size

    fun addCities(cities: ArrayList<City>) {
        this.cities.clear()
        this.cities= cities
        notifyDataSetChanged()
    }

    fun clear() {
        this.cities.clear()
        notifyDataSetChanged()
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tv_province: AppCompatTextView = itemView.findViewById(R.id.spinner_province)
        private var tv_firstLetter: AppCompatTextView = itemView.findViewById(R.id.firstLetter_province)

        fun bind(city: City) {
            val province_name = city.name
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
                onCityClickListener.onCityClick(city)
            }

        }
    }

    interface OnCityClickListener {
        fun onCityClick(city: City)
    }

}