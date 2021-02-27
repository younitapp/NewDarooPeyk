package ir.rosependar.snappdaroo.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.andrognito.flashbar.Flashbar
import com.ouattararomuald.slider.SliderAdapter
import com.ouattararomuald.slider.loaders.glide.GlideImageLoaderFactory
import ir.rosependar.snappdaroo.MainActivity
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.utils.Constants
import ir.rosependar.snappdaroo.utils.Prefs
import ir.rosependar.snappdaroo.utils.errorToast
import ir.rosependar.snappdaroo.utils.l
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : Fragment(), ItemClickListener {
    private var userProfileStatus = 0

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.HomeResponse.observe(viewLifecycleOwner, Observer {
            if ((activity as MainActivity).paidOrderId.isNotEmpty()) {
                findNavController().navigate(
                    R.id.action_homeFragment_to_orderDetailFragment3,
                    Bundle().apply {
                        putString("order_id", (activity as MainActivity).paidOrderId)
                        putParcelableArrayList(
                            "types",
                            it[0].app_prs_types as ArrayList<out Parcelable>
                        )
                    })
                (activity as MainActivity).paidOrderId = ""
                return@Observer
            }
            rv_items.layoutManager = GridLayoutManager(requireContext(), 3)


            fetchData()
        })

    }
    private fun fetchData(){
        viewModel.sliderResponse.observe(requireActivity(), Observer {
            if (it?.body() != null) {
                val sliders = mutableListOf<String>()
                it.body()!!.data.slides.forEach {
                    sliders.add(it.slide_image!!)
                }

                image_slider.adapter = SliderAdapter(
                    requireContext(),
                    GlideImageLoaderFactory(),
                    imageUrls = sliders,
                    descriptions = mutableListOf()
                )
                image_slider.adapter!!.setImageClickListener(object :
                    SliderAdapter.ImageViewClickListener {
                    override fun onItemClicked(
                        sliderId: String,
                        position: Int,
                        imageUrl: String
                    ) {

                        val slider = it.body()!!.data.slides[position]
                        if (slider.slide_link!!.contains("link=")) {

                            val url = slider.slide_link.removePrefix("link=")
                            findNavController().navigate(R.id.action_homeFragment_to_webViewFragment,Bundle().apply {
                                putString("url",url)
                            })
                        }

                    }
                })
                /*   val dividerItemDecoration = MyDividerItemDecorator(
                       ContextCompat.getDrawable(requireContext(),R.drawable.divider_main)!!
                   )
                   rv_items.addItemDecoration(dividerItemDecoration)*/
                rv_items.adapter =
                    ItemTypeAdapter(
                        this,
                        it.body()!!.data.menu,
                        requireActivity() as MainActivity
                    )
                rv_items.isNestedScrollingEnabled = false
                lyt_background.visibility = View.VISIBLE
                animationView.visibility = View.GONE
            }
            else{
                Constants.CantConnectSnackbar(requireActivity(), object : Flashbar.OnActionTapListener {
                    override fun onActionTapped(bar: Flashbar) {
                        bar.dismiss()
                        fetchData()
                    }

                })
            }
        })
    }
    override fun OnTypeClicked(code: Int) {
        userProfileStatus = Prefs.getInstance()!!.getProfileStatus()
        if (userProfileStatus == 1) {
            findNavController().navigate(
                R.id.action_homeFragment_to_submitOrderFragment,
                Bundle().apply {
                    putInt("type_id", code)
                }
            )
        } else {
            errorToast("پیش از سفارش نسخه لطفا پروفایل خود را کامل کنید.")
        }
    }

    override fun OnLinkClicked(link: String) {
        findNavController().navigate(R.id.action_homeFragment_to_webViewFragment,Bundle().apply {
            putString("url",link)
        })
    }


}