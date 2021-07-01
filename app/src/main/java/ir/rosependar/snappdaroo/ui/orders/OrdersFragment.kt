package ir.rosependar.snappdaroo.ui.orders

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.models.CodeName
import ir.rosependar.snappdaroo.utils.errorToast
import ir.rosependar.snappdaroo.utils.l
import kotlinx.android.synthetic.main.orders_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class OrdersFragment : Fragment(), orderClickListener {

    companion object {
        fun newInstance() = OrdersFragment()
    }

    private val viewModel: OrdersViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.orders_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
    }


    private fun fetchData() {

        viewModel.settingsData.observe(viewLifecycleOwner, { settings ->

            val statusCodes = settings[0].app_prs_statuses
            val types = settings[0].app_prs_types
            viewModel.getOrderData().observe(viewLifecycleOwner, {
                if (it != null) {
                    if (it.body()!!.data != null && it.body()?.status == 1) {
                        l(it.body()!!.data.toString())
                        rv_orders.layoutManager = LinearLayoutManager(requireContext())
                        if (it.body()!!.data!!.isNotEmpty())
                            rv_orders.adapter =
                                OrderAdapter(this, it.body()!!.data!!, types, statusCodes)
                        animationView.visibility = View.GONE
                        orders_layout.visibility = View.VISIBLE
                    } else if (it.body()!!.data != null && it.body()?.status == 2) {
                        errorToast("هیچ سفارشی توسط شما ثبت نشده است.")
                        animationView.visibility = View.GONE
                    }
                } else {
                    errorToast("هیچ سفارشی توسط شما ثبت نشده است.")
                    animationView.visibility = View.GONE
                }
            })
        })
    }

    override fun OnOrderClicked(id: Int, types: List<CodeName>) {
        findNavController().navigate(
            R.id.action_ordersFragment_to_orderDetailFragment,
            Bundle().apply {
                putString("order_id", id.toString())
                putParcelableArrayList("types", types as ArrayList<out Parcelable>)
            })
    }

}