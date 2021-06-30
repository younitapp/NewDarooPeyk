package ir.rosependar.snappdaroo.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import ir.rosependar.snappdaroo.MainActivity
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.dialogs.*
import ir.rosependar.snappdaroo.models.Area
import ir.rosependar.snappdaroo.models.City
import ir.rosependar.snappdaroo.models.Country
import ir.rosependar.snappdaroo.models.Province
import ir.rosependar.snappdaroo.ui.login.LoginActivity
import ir.rosependar.snappdaroo.utils.*
import kotlinx.android.synthetic.main.profile_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import saman.zamani.persiandate.PersianDate
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModel()
    private var selectedProvince: Province? = null
    private var selectedCity: City? = null
    private var selectedArea: Area? = null
    private var selectedCountry: Country? = null
    private var selectedBirthDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setFonts()
        l("api token is ${Prefs.getInstance()!!.getToken()}")
        /* btn_logout.setOnClickListener {
             Prefs.getInstance()!!.saveToken("")
             Intent(requireActivity() as MainActivity, LoginActivity::class.java).apply {
                 flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                 startActivity(this)
             }
         }*/
        viewModel.mainLiveData.observe(viewLifecycleOwner, { response ->

            l(response.cities.toString())
            /* ProvinceDialogFragment.newInstance(mutableListOf(),it.provinces!!.data).show(parentFragmentManager,"")*/
            if (response.countries != null && response.cities != null && response.provinces != null && response.profile != null && response.areas != null) {
                if (response.profile.data != null && response.profile.data[0] != null) {

                    val profile = response.profile.data[0]
                    l("milProfileResponse $profile")

                    edt_mobile.editText!!.setText(profile?.property?.tel)
                    edt_name.editText!!.setText(profile?.first_name)
                    edt_last_name.editText!!.setText(profile?.last_name)
                    edt_postalcode.editText!!.setText(profile?.property?.postal_code?.toString())
                    edt_adress.editText!!.setText(profile?.property?.address)
                    edt_insurance.editText!!.setText(profile?.property?.insurance_code)
                    edt_nId.editText!!.setText(profile?.property?.nid.toString())
                    selectProvince(profile?.property?.state?.toInt())
                    selectCity(profile?.property?.city?.toInt())
                    selectArea(profile?.property?.area?.toInt())

                    try {
                        selectBirthDay(profile?.property?.birthday.toString())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                fragmentEditProfile_tv_countryName.text = response.countries[0].name
                fragmentEditProfile_rl_province.setOnClickListener {
                    ProvinceDialogFragment.newInstance(response.cities, response.provinces)
                        .show(parentFragmentManager, "0")
                    ProvinceDialogFragment.listener = object : OnProvincePassDataListener {
                        override fun onProvinceReceive(province: Province) {
                            fragmentEditProfile_tv_provinceName.text = province.name
                            selectedProvince = province
                        }
                    }

                }
                fragmentEditProfile_rl_city.setOnClickListener {
                    if (selectedProvince != null) {
                        CityDialogFragment.newInstance(selectedProvince!!.code)
                            .show(parentFragmentManager, "1")
                        CityDialogFragment.listener = object : OnCityPassDataListener {
                            override fun onCityRecieve(city: City) {
                                fragmentEditProfile_tv_cityName.text = city.name
                                selectedCity = city
                            }
                        }
                    } else {
                        errorToast("استان انتخاب نشده است.")
                    }
                }
                fragmentEditProfile_rl_area.setOnClickListener {
                    if (selectedCity != null) {
                        AreaDialogFragment.newInstance(selectedCity!!.code)
                            .show(parentFragmentManager, "")
                        AreaDialogFragment.listener = object : OnAreaPassDataListener {
                            override fun onAreaRecieve(area: Area) {
                                fragmentEditProfile_tv_areaName.text = area.name
                                selectedArea = area
                            }
                        }
                    } else {
                        errorToast("شهر انتخاب نشده است.")
                    }
                }
                fragmentEditProfile_rl_country.setOnClickListener {
                    if (selectedProvince != null) {
                        CountryDialogFragment.newInstance()
                            .show(parentFragmentManager, "1")
                        CountryDialogFragment.listener = object : OnCountryPassDataListener {
                            override fun onCountryRecieve(country: Country) {
                                fragmentEditProfile_tv_countryName.text = country.name
                                selectedCountry = country
                            }

                        }
                    } else {
                        errorToast("استان انتخاب نشده است.")
                    }
                }
                fragmentEditProfile_rl_birthday.setOnClickListener {
                    val picker = PersianDatePickerDialog(requireContext())
                        .setPositiveButtonString("باشه")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("امروز")
                        .setTodayButtonVisible(true)
                        .setMinYear(1300)
                        .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                        .setActionTextColor(Color.GRAY)
                        .setTypeFace(ResourcesCompat.getFont(requireContext(), R.font.iran_sans))
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(object : Listener {
                            @SuppressLint("SetTextI18n")
                            override fun onDateSelected(persianCalendar: PersianCalendar) {
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                selectedBirthDate = dateFormat.format(persianCalendar.time)
                                l("selected Birthday is $selectedBirthDate")
                                fragmentEditProfile_tv_birthdayName.text =
                                    persianCalendar.persianYear.toString() + "/" + persianCalendar.persianMonth.toString() + "/" + persianCalendar.persianDay.toString()
                            }

                            override fun onDismissed() {}
                        })

                    picker.show()
                }
            }

            lyt_profile.visibility = View.VISIBLE
            btn_save_profile.visibility = View.VISIBLE
            animationView.visibility = View.GONE
        })
        btn_save_profile.setOnClickListener {
            if (
                Validation.checkName(edt_name.editText!!) &&
                Validation.checkName(edt_last_name.editText!!) &&
                Validation.checkPostalCode(edt_postalcode.editText!!) &&
                Validation.checkAddress(edt_adress.editText!!) &&
                Validation.checkMelliCode(edt_nId.editText!!)
            ) {
                viewModel.saveProfile(
                    first_name = edt_name.editText!!.text.toString(),
                    mobile = edt_mobile.editText!!.text.toString(),
                    last_name = edt_last_name.editText!!.text.toString(),
                    country = selectedCountry?.code.toString(),
                    provinceId = selectedProvince?.code.toString(),
                    cityId = selectedCity?.code.toString(),
                    areaCode = selectedArea?.code.toString(),
                    address = edt_adress.editText!!.text.toString(),
                    postal_code = edt_postalcode.editText!!.text.toString(),
                    gender = "",
                    birthDay = selectedBirthDate!!,
                    insuranceCode = edt_insurance.editText!!.text.toString(),
                    nId = edt_nId.editText!!.text.toString()
                )
                    .observe(viewLifecycleOwner, Observer {
                        if (it?.body() != null && it.body()?.status == 1) {
                            Prefs.getInstance()!!.saveProfileStatus(1)
                            successToast("حساب کاربری شما با موفقیت ذخیره شد.")
                        } else {
                            errorToast("مشکلی در ذخیره ی حساب کاربری شما بوجود آمده است ، لطفا بعدا امتحان کنید.")
                        }
                    })
            }
        }
    }

    private fun setFonts() {
        edt_name.typeface = ResourcesCompat.getFont(requireContext(), R.font.iran_sans)
        edt_postalcode.typeface = ResourcesCompat.getFont(requireContext(), R.font.iran_sans)
        edt_insurance.typeface = ResourcesCompat.getFont(requireContext(), R.font.iran_sans)
        edt_last_name.typeface = ResourcesCompat.getFont(requireContext(), R.font.iran_sans)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun selectBirthDay(birthday: String) {


        val splittedText = birthday.toString().split("T")

        l("birthday is $birthday")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val georgydate = dateFormat.parse(birthday)!!
        val date = dateFormat.format(georgydate)
        val persianDate = PersianDate(georgydate.time)

        /*val splitters = birthday.split("-")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")*/
        val showDate = "${persianDate.shYear}/${persianDate.shMonth}/${persianDate.shDay}"
        fragmentEditProfile_tv_birthdayName.text = showDate
        selectedBirthDate = date.toString()
        l("selected Birthday is $selectedBirthDate")

    }

    fun selectProvince(id: Int?) {
        if (id != null)
            viewModel.findProvinceById(id).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    fragmentEditProfile_tv_provinceName.text = it.name
                    selectedProvince = it
                }
            })
    }

    fun selectCity(id: Int?) {
        if (id != null)
            viewModel.findCityById(id).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    fragmentEditProfile_tv_cityName.text = it.name
                    selectedCity = it
                }
            })
    }

    fun selectArea(code: Int?) {
        if (code != null)
            viewModel.findAreaByCode(code).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    fragmentEditProfile_tv_areaName.text = it.name
                    selectedArea = it
                }
            })
    }
}