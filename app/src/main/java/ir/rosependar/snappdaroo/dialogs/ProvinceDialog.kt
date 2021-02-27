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
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class ProvinceDialogFragment: DialogFragment(),
    ProvinceListAdapter.OnProvinceClickListener {

    private lateinit var edt_searchInput    : AppCompatEditText
    private lateinit var recyclerView       : RecyclerView
    private lateinit var provinceListAdapter: ProvinceListAdapter
    private val viewModel : ProfileViewModel by viewModel()
    private val cities by lazy { requireArguments().getParcelableArrayList<City>("cities") }
    private val provinces by lazy { requireArguments().getParcelableArrayList<Province>("provinces") }
    companion object {

        var listener: OnProvincePassDataListener? = null

        fun newInstance( cities : List<City> , provinces : List<Province>): ProvinceDialogFragment {
            val fragment =
                ProvinceDialogFragment()
            val args = Bundle().apply {
                putParcelableArrayList("cities" , cities as ArrayList<City>)
                putParcelableArrayList("provinces" , provinces as ArrayList<Province>)
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
        provinceListAdapter = ProvinceListAdapter(this)
        recyclerView.adapter = provinceListAdapter
        viewModel.searchInProvinces("%%").observe(viewLifecycleOwner,
            Observer {
                provinceListAdapter.addProvinces(it!! as ArrayList<Province>)
            })


        edt_searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(input: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchInProvinces("%${input.toString()}%").observe(viewLifecycleOwner,
                    Observer {
                        provinceListAdapter.addProvinces(it!! as ArrayList<Province>)
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

    override fun onProvinceClick(province: Province) {
        listener?.onProvinceReceive(province)
        dismissAllowingStateLoss()
    }



}
interface OnProvincePassDataListener {
    fun onProvinceReceive(province: Province)
}