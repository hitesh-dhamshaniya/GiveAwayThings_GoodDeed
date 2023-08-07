package give.away.good.deeds.screens.mylisting

import androidx.lifecycle.ViewModelProvider
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmnetMyListingBinding
import give.away.good.deeds.network.model.MyGiveAwayModel

class MyListingFragment : BaseFragment<FragmnetMyListingBinding, MyListingViewModel>() {
    override fun getLayout(): Int = R.layout.fragmnet_my_listing

    override fun getViewModel(): MyListingViewModel {
        return ViewModelProvider(this)[MyListingViewModel::class.java]
    }

    override fun onCreateView() {
        val myGiveAwayList: MutableList<MyGiveAwayModel> = mutableListOf()
        mViewDataBinding.rvMyListing.adapter = MyGiveAwayListAdapter(myGiveAwayList)
    }

    override fun initViewModel() {
    }
}