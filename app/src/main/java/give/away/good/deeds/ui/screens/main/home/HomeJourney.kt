package give.away.good.deeds.ui.screens.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import give.away.good.deeds.ui.screens.main.post.detail.PostDetailScreen
import give.away.good.deeds.ui.screens.main.post.list.PostListScreen
import give.away.good.deeds.ui.theme.AppTheme
import give.away.good.deeds.utils.contentView

class HomeJourney : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = contentView(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)) {
        AppTheme {
            HomeJourneyScreen()
        }
    }
}


@Composable
fun HomeJourneyScreen(
    navController: NavHostController = rememberNavController()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                PostListScreen(
                    navigateToDetail = {
                        navController.navigate("post_detail")
                    }
                )
            }

            composable("post_detail") {
                PostDetailScreen(
                    onBackPress = {
                        navController.popBackStack()
                    },
                )
            }



        }
    }
}
