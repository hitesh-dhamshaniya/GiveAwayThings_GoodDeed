package give.away.good.deeds.ui.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import give.away.good.deeds.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var bottomNavigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        bottomNavigationView = navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.postAuthFragmentContainer) as? NavHostFragment
        navHostFragment?.navController?.let { navController ->
            navView.setupWithNavController(navController)
        }

        detectKeyboard()
    }

    private fun detectKeyboard() {
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView.rootView) { _, insets ->
            //This lambda block will be called, every time keyboard is opened or closed
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            bottomNavigationView?.isVisible = !imeVisible
            insets
        }
    }
}