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
import ir.rosependar.snappdaroo.models.Province
import ir.rosependar.snappdaroo.ui.profile.ProfileViewModel
import ir.rosependar.snappdaroo.utils.l
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class CityDialogFragment : DialogFragment(),
    CityListAdapter.OnCityClickListener {

    private lateinit var edt_searchInput: AppCompatEditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var cityListAdapter: CityListAdapter
    private val viewModel: ProfileViewModel by viewModel()
    private val StateId by lazy { requireArguments().getLong("state_id") }

    companion object {

        var listener: OnCityPassDataListener? = null

        fun newInstance(stateId : Long): CityDialogFragment {
            val fragment =
                CityDialogFragment()
            val args = Bundle().apply {
                putLong("state_id" ,stateId)
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
        cityListAdapter = CityListAdapter(this)
        recyclerView.adapter = cityListAdapter
        viewModel.getCitiesByStateId(StateId).observe(viewLifecycleOwner,
            Observer {
                cityListAdapter.addCities(it!! as ArrayList<City>)
                l("Cities size in dialog = ${it.size} and state id is = $StateId")

            })


        edt_searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(input: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchInCities(StateId,"%${input.toString()}%").observe(viewLifecycleOwner,
                    Observer { response ->
                        cityListAdapter.addCities(response as ArrayList<City>)
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


    override fun onCityClick(city: City) {
        listener?.onCityRecieve(city)
        dismissAllowingStateLoss()
    }


}

interface OnCityPassDataListener {
    fun onCityRecieve(city: City)
}
