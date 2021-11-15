package ir.rosependar.snappdaroo.ui.orders.orderdetail

import android.content.res.ColorStateList
import android.graphics.Color
import android.media.Image
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.models.CodeName
import ir.rosependar.snappdaroo.utils.PriceUtil
import ir.rosependar.snappdaroo.utils.errorToast
import ir.rosependar.snappdaroo.utils.l
import ir.rosependar.snappdaroo.utils.successToast
import kotlinx.android.synthetic.main.check_out_fragment.*
import kotlinx.android.synthetic.main.order_detail_fragment.*
import kotlinx.android.synthetic.main.order_detail_fragment.animationView
import kotlinx.android.synthetic.main.order_detail_fragment.btn_back
import kotlinx.android.synthetic.main.order_detail_fragment.btn_pay
import kotlinx.android.synthetic.main.order_detail_fragment.order_history_createdAt
import kotlinx.android.synthetic.main.order_detail_fragment.order_history_orderNumber
import kotlinx.android.synthetic.main.order_detail_fragment.order_history_productName
import kotlinx.android.synthetic.main.order_detail_fragment.order_history_status
import kotlinx.android.synthetic.main.order_detail_fragment.txt_comments
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat

private const val TAG = "OrderDetailFragment"

class OrderDetailFragment : Fragment() {

    companion object {
        fun newInstance() = OrderDetailFragment()
    }

    private val orderId by lazy { requireArguments().getString("order_id") }
    private val types by lazy { requireArguments().getParcelableArrayList<CodeName>("types") }
    private val viewModel: OrderDetailViewModel by viewModel() {
        parametersOf(orderId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }

        Log.e(TAG, "myOrderId: $orderId")

        try {
            viewModel.orderData.observe(viewLifecycleOwner, { response ->
                if (response?.body() != null && response.body()!!.status == 1 && response.body()!!.data != null) {
                    val order = response.body()?.data!!.prescription!![0]
                    l("milMyOrder $order")
                    var color = 0
                    when (order.status_id) {
                        1 -> {
                            color = Color.parseColor("#A9A9A9")
                        }
                        2 -> {
                            color = Color.parseColor("#007CFF")
                        }
                        3 -> {
                            color = Color.parseColor("#FFAA00")

                        }
                        4 -> {
                            color = Color.parseColor("#00FFA6")
                        }
                        5 -> {
                            color = Color.parseColor("#003EFF")
                        }
                        6 -> {
                            color = Color.parseColor("#FF003A")
                        }
                        7 -> {
                            color = Color.parseColor("#8700FF")
                        }
                        8 -> {
                            color = Color.parseColor("#02B4A9")
                        }
                        9 -> {
                            color = Color.parseColor("#C69300")
                        }
                    }

                    if (order.status_id == 1) {
                        btn_delete.visibility = View.VISIBLE
                        btn_delete.setOnClickListener {
                            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                .setMessage("آیا مطمئن هستید؟")
                                .setCancelable(true)
                                .setPositiveButton(
                                    "بله"
                                ) { _, _ -> viewModel.deleteOrder(order.id.toString()) }
                                .setNegativeButton(
                                    "خیر"
                                ) { _, _ -> }
                                .create().show()
                            observeDeleteResponse()
                        }

                    } else {
                        btn_delete.visibility = View.GONE
                    }

                    order_history_createdAt.text = order.created_at
                    order_history_status.setTextColor(color)
                    img_status.backgroundTintList = ColorStateList.valueOf(color)

                    order_history_prescriptionName.text = Html.fromHtml(order.prescription_text)
                    order_history_orderNumber.text = "شماره درخواست : ${orderId}"
                    val typeText = types?.find {
                        it.code == order.pr_type
                    }
                    order_history_productName.text = typeText?.name
                    if (order.images != null && order.images.isNotEmpty()) {
                        val image = order.images[0]
                        lyt_img_order.visibility = View.VISIBLE
                        Glide.with(img_order).load(image.path).into(img_order)
                        img_order.setOnClickListener {

                            val images = arrayOf(order.images[0].path)
                            StfalconImageViewer.Builder<String>(
                                requireContext(),
                                images
                            ) { view, image ->
                                Glide.with(requireContext()).load(images[0]).into(view)
                            }
                                .withTransitionFrom(img_order)
                                .show()
                        }
                    }
                    order_history_comment.text = order.comments
                    viewModel.settingsData.observe(viewLifecycleOwner, Observer {
                        val status = it[0].app_prs_statuses.find {
                            it.code == order.status_id
                        }
                        order_history_status.text = status?.name
                        if (status?.code == 3) {
                            btn_pay.visibility = View.VISIBLE
                            btn_pay.setOnClickListener {
                                try {
                                    findNavController().navigate(
                                        R.id.action_orderDetailFragment_to_checkOutFragment,
                                        Bundle().apply {
                                            putString("order_id", orderId)
                                        })
                                } catch (e: Exception) {
                                    findNavController().navigate(
                                        R.id.action_orderDetailFragment3_to_checkOutFragment2,
                                        Bundle().apply {
                                            putString("order_id", orderId)
                                        })
                                }
                            }
                        } else if (status!!.code >= 4 && response.body()?.data!!.checkout != null) {
                            if (status.code == 4) {
                                txt_status.visibility == View.VISIBLE
                            }
                            if (response.body() != null || response.body()?.data != null) {
                                if (!response.body()?.data?.checkout.isNullOrEmpty()) {
                                    lyt_successfull.visibility = View.VISIBLE
                                    val checkout = response.body()?.data?.checkout!![0]
                                    val totalPrice = PriceUtil.getThousandSeparator()
                                        .format(checkout.total_price / 10)
                                    val shippingPrice = PriceUtil.getThousandSeparator()
                                        .format(checkout.shipping_price / 10)
                                    total_price.text = "هزینه پرداختی : ${totalPrice} تومان"
                                    shipping_price.text = "هزینه ارسال : ${shippingPrice} تومان"
                                    if (!checkout.comments.isNullOrEmpty()) {
                                        txt_comments.visibility = View.VISIBLE
                                        txt_comments.text = "توضیحات مرکز : ${checkout.comments}"
                                    } else {
                                        txt_comments.visibility = View.GONE
                                    }

                                }
                            }
                        }
                    })

                }
                lyt_detail.visibility = View.VISIBLE
                animationView.visibility = View.GONE
            })
        } catch (e: Exception) {
            findNavController().popBackStack()
        }
    }

    private fun observeDeleteResponse() {
        viewModel.responseDeleteOrder.observe(viewLifecycleOwner, {
            if (it != null && it.isSuccessful) {
                if (it.body()!!.status == 1) {
                    successToast("درخواست با موفقیت حذف شد")
                    findNavController().popBackStack()
                } else {
                    errorToast("مشکلی در حذف پیش آمده. دوباره تلاش کنید")
                }
            }
        })
    }

}