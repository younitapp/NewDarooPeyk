package ir.rosependar.snappdaroo.ui.bonus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import ir.rosependar.snappdaroo.databinding.BonusFragmentBinding
import ir.rosependar.snappdaroo.utils.errorToast
import ir.rosependar.snappdaroo.utils.successToast
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

            viewModel.bonusCode.observe(viewLifecycleOwner, object : Observer<String> {
                override fun onChanged(bonusCode: String?) {

                    if (!bonusCode.isNullOrEmpty() && bonusCode != "") {
                        if (bonusCode.length > 30) errorToast("کد وارد شده اشتباه است")
                        else {
                            Toast.makeText(requireContext(), "لطفا صبر کنید...", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.bonusResponse(bonusCode).observe(viewLifecycleOwner, {
                                if (it?.body() != null && it.isSuccessful) {
                                    it.body()?.apply {
                                        when (status) {
                                            1 -> {
                                                successToast("کد تخفیف با موفقیت ثبت شد")
                                                dismiss()
                                            }
                                            -2 -> errorToast("کد تخفیف قبلا ثبت شده است")
                                            else -> errorToast("کد تخفیف نا معتبر است")
                                        }
                                    }
                                    Log.e(TAG, "myStatus: ${it.body()!!.status} ")
                                    Log.e(TAG, "myMsg: ${it.body()!!.message} ")
                                    Log.e(TAG, "myCode: $bonusCode ")
                                }
                            })
                        }
                    }

                    viewModel.bonusCode.removeObserver(this)
                }

            })
        }
    }


}