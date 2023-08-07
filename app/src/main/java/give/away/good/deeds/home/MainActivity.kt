package give.away.good.deeds.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseActivity
import give.away.good.deeds.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun getViewModel(): MainViewModel {
        return ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val toolbar: Toolbar = findViewById(R.id.toolbarDashboard)
        setSupportActionBar(toolbar)

        settingBottomNav()
    }

    private fun settingBottomNav() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.postAuthFragmentContainer) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment, R.id.searchFragment, R.id.addPostFragment, R.id.messageFragment, R.id.settingsFragment
        ).build()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }
}