package give.away.good.deeds.ui.screens.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import give.away.good.deeds.R
import give.away.good.deeds.messaging.requestNotificationPermission
import give.away.good.deeds.messaging.subscribeToTopic
import give.away.good.deeds.network.model.Notification
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var bottomNavigationView: BottomNavigationView? = null
    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        bottomNavigationView = navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.postAuthFragmentContainer) as? NavHostFragment
        navHostFragment?.navController?.let { navController ->
            navView.setupWithNavController(navController)
        }

        setupApp()
    }

    private fun setupApp() {
        // Hide keyboard when bottom nav is opened
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView.rootView) { _, insets ->
            //This lambda block will be called, every time keyboard is opened or closed
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            bottomNavigationView?.isVisible = !imeVisible
            insets
        }

        requestNotificationPermission()

        subscribeToTopic()

        // add
        viewModel.saveToken()
    }

    @Suppress("DEPRECATION")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.getParcelable<Notification>("data")?.let { notification ->
            Log.e("MainActivity", notification.toString())
        }
        // bottomNavigationView?.selectedItemId =  R.id.addPostFragment
    }

}