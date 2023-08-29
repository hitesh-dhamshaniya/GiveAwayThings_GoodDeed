package give.away.good.deeds.ui.screens.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import give.away.good.deeds.R
import give.away.good.deeds.ui.screens.authentication.module.authModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class LandingActivity : AppCompatActivity(R.layout.activity_landing) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(authModule)
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(authModule)
    }

}