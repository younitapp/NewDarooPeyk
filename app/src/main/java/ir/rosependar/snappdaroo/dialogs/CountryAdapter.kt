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
import ir.rosependar.snappdaroo.models.Country
import java.util.*


class CountryListAdapter(private var onCityClickListener: OnCountryClickListener) :
    RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {

    private var countries =  ArrayList<Country>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder =
        CountryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_spinner_province_row, parent, false)
        )

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) =
        holder.bind(countries[position])

    override fun getItemCount(): Int = countries.size

    fun addCountries(countries: ArrayList<Country>) {
        this.countries.clear()
        this.countries= countries
        notifyDataSetChanged()
    }

    fun clear() {
        this.countries.clear()
        notifyDataSetChanged()
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tv_province: AppCompatTextView = itemView.findViewById(R.id.spinner_province)
        private var tv_firstLetter: AppCompatTextView = itemView.findViewById(R.id.firstLetter_province)

        fun bind(country: Country) {
            val province_name = country.name
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
                onCityClickListener.onCityClick(country)
            }

        }
    }

    interface OnCountryClickListener {
        fun onCityClick(country: Country)
    }

}