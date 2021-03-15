package ir.rosependar.snappdaroo.ui.contacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.rosependar.snappdaroo.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsFragment : Fragment() {

    private val viewModel: ContactsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contacts_fragment, container, false)
    }


}