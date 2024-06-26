package give.away.good.deeds

import android.app.Application
import give.away.good.deeds.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GoodDeedsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    /**
     * Koin Init
     */
    private fun initKoin() {
        // start Koin!
        startKoin {

            // Android context
            androidContext(this@GoodDeedsApp)

            // modules
            modules(appComponent)
        }
    }
}