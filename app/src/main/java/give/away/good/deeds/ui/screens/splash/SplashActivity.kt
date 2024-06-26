package give.away.good.deeds.ui.screens.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import give.away.good.deeds.R
import give.away.good.deeds.ui.screens.app_common.launchActivity
import give.away.good.deeds.ui.screens.authentication.AuthActivity
import give.away.good.deeds.ui.screens.main.MainActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val viewModel by inject<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.preProcessing()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect {
                    startActivity(it)
                }
            }
        }
    }

    private fun startActivity(isLoggedIn: Boolean) {
        val activity = if (isLoggedIn) MainActivity::class else AuthActivity::class
        launchActivity(activity)
        finish()
    }

}