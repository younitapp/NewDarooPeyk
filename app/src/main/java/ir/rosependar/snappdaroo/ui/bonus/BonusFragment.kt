package ir.rosependar.snappdaroo.ui.bonus

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import ir.rosependar.snappdaroo.R
import ir.rosependar.snappdaroo.databinding.BonusFragmentBinding
import ir.rosependar.snappdaroo.utils.errorToast
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "BonusFragment"

class BonusFragment : DialogFragment() {

    private val viewModel: BonusViewModel by viewModel()
    private lateinit var binding: BonusFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BonusFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        clickOnSaveBtn()

        return binding.root
    }

    private fun clickOnSaveBtn() {
        binding.btnSave.setOnClickListener {
            viewModel.bonusCode.observe(viewLifecycleOwner, { bonusCode ->
                if (!bonusCode.isNullOrEmpty() && bonusCode != "") {
                    if (bonusCode.length > 30) errorToast("کد وارد شده اشتباه است")
                    else {
                        viewModel.bonusResponse.observe(viewLifecycleOwner, {
                            if (it?.body() != null && it.isSuccessful) {
                                Log.e(TAG, "myStatus: ${it.body()!!.status} ")
                                Log.e(TAG, "myMsg: ${it.body()!!.message} ")
                                Log.e(TAG, "myCode: $bonusCode ")
                            }
                        })
                    }
                }
            })
        }
    }


}