package give.away.good.deeds.screens.landing

import androidx.lifecycle.ViewModelProvider
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseActivity
import give.away.good.deeds.databinding.ActivityLandingBinding

class LandingActivity : BaseActivity<ActivityLandingBinding, LandingViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_landing

    override fun getViewModel(): LandingViewModel {
        return ViewModelProvider(this)[LandingViewModel::class.java]
    }

}