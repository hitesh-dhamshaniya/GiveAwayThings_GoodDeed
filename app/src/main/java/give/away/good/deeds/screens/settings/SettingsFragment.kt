package give.away.good.deeds.screens.settings

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import give.away.good.deeds.core.extension.getVersionName
import give.away.good.deeds.R
import give.away.good.deeds.core.RecyclerItemClickListener
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmentSettingsBinding
import give.away.good.deeds.network.model.SettingItemModel
import give.away.good.deeds.network.model.SettingUtil
import give.away.good.deeds.screens.search.SearchViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SearchViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_settings

    override fun getViewModel(): SearchViewModel {
        return ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun initViewModel() {

    }

    override fun onCreateView() {

        val settingsAdapter = SettingsAdapter(SettingUtil.getSettingItems(), recyclerViewItemClick)
        mViewDataBinding.rvSettings.adapter = settingsAdapter

        mViewDataBinding.tvVersionInfo.text = mActivity.getVersionName()
    }

    private val recyclerViewItemClick: RecyclerItemClickListener = object : RecyclerItemClickListener {
        override fun <T> onItemClick(position: Int, item: T) {
            if (item is SettingItemModel) {
                when ((item as SettingItemModel).title) {
                    SettingUtil.PROFILE -> {
                        navigate(R.id.action_settingsFragment_to_profileFragment)
                    }

                    SettingUtil.CHANGE_PASSWORD -> {
                        navigate(R.id.action_settingsFragment_to_changePasswordFragment)
                    }

                    SettingUtil.SET_LOCATION -> {
                        navigate(R.id.action_settingsFragment_to_setupLocationFragment)
                    }
                }
            }
        }
    }

    /**
     * Navigate to destination fragment
     */
    private fun navigate(id: Int) {
        findNavController().navigate(id)
    }
}