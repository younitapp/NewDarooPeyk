package ir.rosependar.snappdaroo.ui.checkout

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.utils.Constants
import ir.rosependar.snappdaroo.utils.PriceUtil
import ir.rosependar.snappdaroo.utils.successToast
import kotlinx.android.synthetic.main.check_out_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CheckOutFragment : Fragment() {
    val prescriptionId by lazy { requireArguments().getString("order_id") }
    companion object {
        fun newInstance() = CheckOutFragment()
    }

    private val viewModel: CheckOutViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.check_out_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getCheckOutFactor(prescriptionId!!).observe(viewLifecycleOwner, Observer {
            if(it?.body()?.status == 1){
                val response = it.body()!!.data
                txt_order_price.text = PriceUtil.getThousandSeparator().format(response.order_price / 10) + " تومان"
                txt_shipping_price.text = PriceUtil.getThousandSeparator().format(response.shipping_price / 10)+ " تومان"
                txt_total_price.text = PriceUtil.getThousandSeparator().format(response.total_price / 10)+ " تومان"
                btn_pay.setOnClickListener {
                    viewModel.getRequestPayment(prescriptionId!!).observe(viewLifecycleOwner,
                        Observer {
                            if(it?.body()?.status == 1) {
                                val browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse("${Constants.SITE_URL}gateway/request/${it.body()!!.data.resnum}/app"))
                                startActivity(browserIntent)
                            }
                        })
                }
                if(!response.comments.isNullOrEmpty()){
                    txt_comments.visibility = View.VISIBLE
                    txt_comments.text = "توضیحات مرکز : ${response.comments}"
                }else{
                    txt_comments.visibility = View.GONE
                }
            }
            lyt_checkout.visibility = View.VISIBLE
            animationView.visibility = View.GONE
        })
    }

}