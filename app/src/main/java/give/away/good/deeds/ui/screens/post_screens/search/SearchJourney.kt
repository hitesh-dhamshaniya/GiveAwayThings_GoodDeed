package give.away.good.deeds.ui.screens.post_screens.search

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
import give.away.good.deeds.ui.screens.post_screens.post.detail.PostDetailScreen
import give.away.good.deeds.ui.screens.post_screens.post.search.PostSearchScreen
import give.away.good.deeds.ui.theme.AppTheme
import give.away.good.deeds.utils.contentView

class SearchJourney : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = contentView(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)) {
        AppTheme {
            SearchJourneyScreen()
        }
    }
}


@Composable
fun SearchJourneyScreen(
    navController: NavHostController = rememberNavController()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = "search"
        ) {
            composable("search") {
                PostSearchScreen(
                    navigateToDetail = {
                        navController.navigate("post_detail")
                    }
                )
            }

            composable("post_detail") {
                PostDetailScreen(
                    onBackPress = {
                        navController.popBackStack()
                    }
                )
            }

        }
    }
}
