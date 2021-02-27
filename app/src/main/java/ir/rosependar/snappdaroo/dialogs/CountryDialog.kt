package ir.rosependar.snappdaroo.dialogs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.models.City
import ir.rosependar.snappdaroo.models.Country
import ir.rosependar.snappdaroo.ui.profile.ProfileViewModel
import ir.rosependar.snappdaroo.utils.l
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList
class CountryDialogFragment : DialogFragment(), CountryListAdapter.OnCountryClickListener {

private lateinit var edt_searchInput: AppCompatEditText
private lateinit var recyclerView: RecyclerView
private lateinit var countryListAdapter: CountryListAdapter
private val viewModel: ProfileViewModel by viewModel()

companion object {

    var listener: OnCountryPassDataListener? = null

    fun newInstance(): CountryDialogFragment {
        val fragment =
            CountryDialogFragment()
        val args = Bundle().apply {

        }
        fragment.arguments = args
        return fragment
    }
}

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    return inflater.inflate(R.layout.fragment_province, container, false)
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)


    edt_searchInput = view.findViewById(R.id.fragmentProvince_edt_searchInput)
    recyclerView = view.findViewById(R.id.fragmentProvince_rv)
    recyclerView.layoutManager = LinearLayoutManager(activity)
    countryListAdapter = CountryListAdapter(this)
    recyclerView.adapter = countryListAdapter
    viewModel.getCountries().observe(viewLifecycleOwner,
        Observer {
            countryListAdapter.addCountries(it!! as ArrayList<Country>)
        })


    edt_searchInput.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(input: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.searchInCountries("%${input.toString()}%").observe(viewLifecycleOwner,
                Observer { response ->
                    countryListAdapter.addCountries(response as ArrayList<Country>)
                })
        }
    })

}

override fun onStart() {
    super.onStart()
    if (dialog != null) {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}




    override fun onCityClick(country: Country) {
        listener?.onCountryRecieve(country)
        dismissAllowingStateLoss()
    }


}

interface OnCountryPassDataListener {
    fun onCountryRecieve(country: Country)
}