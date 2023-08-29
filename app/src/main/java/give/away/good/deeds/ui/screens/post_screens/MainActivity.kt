package give.away.good.deeds.ui.screens.post_screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import give.away.good.deeds.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.postAuthFragmentContainer) as? NavHostFragment
        navHostFragment?.navController?.let { navController ->
            navView.setupWithNavController(navController)
        }
    }
}