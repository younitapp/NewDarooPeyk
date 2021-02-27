package ir.rosependar.snappdaroo.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.rosependar.snappdaroo.R
import kotlinx.android.synthetic.main.fragment_select_image_dialog.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SelectImageDialog : BottomSheetDialogFragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_camera.setOnClickListener {
            listener?.OnBtnCameraClicked()
            dismiss()
        }

        btn_gallery.setOnClickListener {
            listener?.OnBtnGalleryClicked()
            dismiss()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_image_dialog, container, false)
    }

    companion object {
        var listener: SelectImageListener? = null
        @JvmStatic
        fun newInstance() =
            SelectImageDialog().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
interface SelectImageListener{
    fun OnBtnCameraClicked(){}
    fun OnBtnGalleryClicked(){}
}