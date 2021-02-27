package ir.rosependar.snappdaroo.dialogs

import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.models.CodeName
import ir.rosependar.snappdaroo.utils.l
import kotlinx.android.synthetic.main.fragment_time_picker_list_dialog.*
import java.util.ArrayList


const val ARG_ITEM_COUNT = "items"

class TimePicker : BottomSheetDialogFragment() {
    val list by lazy {  requireArguments().getParcelableArrayList<CodeName>(ARG_ITEM_COUNT) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_picker_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_items.layoutManager =
            LinearLayoutManager(context)
        l(list!!.toString())
        rv_items.adapter = CodeNameAdapter(list!!)
    }

    private inner class ViewHolder internal constructor(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.fragment_time_picker_list_dialog_item,
            parent,
            false
        )
    ) {

        internal val text: TextView = itemView.findViewById(R.id.text)
        internal val crd_main: CardView = itemView.findViewById(R.id.crd_main)
    }

    private inner class CodeNameAdapter internal constructor(private val items: List<CodeName>) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = items[position].name
            holder.crd_main.setOnClickListener {
                timeListener?.OnTimePickerClicked(items[position])
                dismiss()
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    companion object {
        var timeListener : TimePickerListener? = null
        fun newInstance(delieveryTimes: List<CodeName>): TimePicker =
            TimePicker().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_ITEM_COUNT, delieveryTimes as ArrayList<out Parcelable>)
                }
            }

    }
}
interface TimePickerListener{
    fun OnTimePickerClicked(time : CodeName)
}