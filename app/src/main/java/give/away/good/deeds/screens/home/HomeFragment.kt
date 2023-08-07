package give.away.good.deeds.screens.home

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmentHomeBinding
import give.away.good.deeds.network.model.GiveAwayModel
import give.away.good.deeds.sharePref.service.ProfilePrefService
import org.koin.android.ext.android.inject

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
        val profilePrefService: ProfilePrefService by inject<ProfilePrefService>()
        Log.e("Login user", "Get User ${profilePrefService.getUser().toString()}")
    }
}