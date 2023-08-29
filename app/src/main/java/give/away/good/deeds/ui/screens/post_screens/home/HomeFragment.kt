package give.away.good.deeds.ui.screens.post_screens.home

import androidx.lifecycle.ViewModelProvider
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmentHomeBinding
import give.away.good.deeds.network.model.GiveAwayModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_home

    override fun getViewModel(): HomeViewModel {
        return ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView() {
        val items: MutableList<GiveAwayModel> = mutableListOf()
        mViewDataBinding.rvGiveAwayItems.adapter = GiveAwayAdapter(items)
    }

    override fun initViewModel() {

    }
}