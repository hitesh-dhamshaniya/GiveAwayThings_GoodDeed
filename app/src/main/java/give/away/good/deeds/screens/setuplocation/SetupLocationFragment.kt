package give.away.good.deeds.screens.setuplocation

import androidx.lifecycle.ViewModelProvider
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmentSetupLocationBinding
import give.away.good.deeds.screens.settings.SettingsViewModel

class SetupLocationFragment : BaseFragment<FragmentSetupLocationBinding, SettingsViewModel>() {
    override fun getLayout(): Int = R.layout.fragment_setup_location

    override fun getViewModel(): SettingsViewModel {
        return ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun initViewModel() {

    }

    override fun onCreateView() {

    }
}