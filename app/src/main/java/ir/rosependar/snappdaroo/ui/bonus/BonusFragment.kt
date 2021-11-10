package ir.rosependar.snappdaroo.ui.bonus

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ir.rosependar.snappdaroo.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class BonusFragment : DialogFragment() {

    private val viewModel: BonusViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bonus_fragment, container, false)
    }


}