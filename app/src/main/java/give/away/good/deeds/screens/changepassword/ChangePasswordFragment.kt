package give.away.good.deeds.screens.changepassword

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding, ChangePasswordViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_change_password

    override fun getViewModel(): ChangePasswordViewModel {
        return ViewModelProvider(this)[ChangePasswordViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView() {
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        super.onContextItemSelected(item)
        if (item.itemId == android.R.id.home) {
            findNavController().popBackStack()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            findNavController().popBackStack()
        }
        return true
    }

    override fun initViewModel() {

    }
}