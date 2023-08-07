package give.away.good.deeds.screens.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import give.away.good.deeds.R
import give.away.good.deeds.screens.landing.LandingActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

                startActivity(Intent(this, LandingActivity::class.java))
                finish()

        },1000)
    }

}