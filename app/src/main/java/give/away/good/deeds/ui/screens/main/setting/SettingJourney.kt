package give.away.good.deeds.ui.screens.main.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import give.away.good.deeds.R
import give.away.good.deeds.ui.screens.app_common.SimpleAlertDialog
import give.away.good.deeds.ui.screens.authentication.AuthActivity
import give.away.good.deeds.ui.screens.main.post.detail.PostDetailScreen
import give.away.good.deeds.ui.screens.main.setting.changepassword.ChangePasswordScreen
import give.away.good.deeds.ui.screens.main.setting.location.SetupLocationScreen
import give.away.good.deeds.ui.screens.main.post.mypost.MyPostScreen
import give.away.good.deeds.ui.screens.main.setting.profile.ProfileScreen
import give.away.good.deeds.ui.screens.main.setting.menu.SettingViewModel
import give.away.good.deeds.ui.screens.main.setting.menu.SettingsScreen
import give.away.good.deeds.ui.screens.main.setting.notification.NotificationScreen
import give.away.good.deeds.ui.theme.AppTheme
import give.away.good.deeds.utils.contentView
import org.koin.androidx.compose.koinViewModel

class SettingsJourney : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = contentView(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)) {
        AppTheme {
            SettingsJourneyScreen()
        }
    }
}

@Composable
fun SettingsJourneyScreen(
    viewModel: SettingViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        val showDialog = remember { mutableStateOf(false) }

        val activity = (LocalContext.current as? Activity)
        if (showDialog.value)
            SimpleAlertDialog(
                title = stringResource(id = R.string.app_name),
                message = stringResource(id = R.string.msg_are_you_sure_you_want_to_logout),
                confirmAction = "Logout",
                onDismiss = {
                    showDialog.value = false
                },
                onConfirm = {
                    viewModel.logout()
                    activity?.finish()
                    activity?.startActivity(Intent(activity, AuthActivity::class.java))
                }
            )

        NavHost(
            navController = navController,
            startDestination = "main"
        ) {

            composable("main") {
                SettingsScreen(
                    onNavigate = { route ->
                        if (route == "logout") {
                            showDialog.value = true
                        } else {
                            navController.navigate(route)
                        }
                    }
                )
            }

            composable("profile") {
                ProfileScreen(
                    onBackPress = {
                        navController.popBackStack()
                    }
                )
            }

            composable("change_password") {
                ChangePasswordScreen(
                    onBackPress = {
                        navController.popBackStack()
                    }
                )
            }

            composable("location") {
                SetupLocationScreen(
                    onBackPress = {
                        navController.popBackStack()
                    }
                )
            }

            composable("notification") {
                NotificationScreen(
                    navController = navController,
                )
            }

            composable("account_listing") {
                MyPostScreen(
                    onBackPress = {
                        navController.popBackStack()
                    },
                    onPostClick = { post ->
                        navController.navigate("post_detail/${post.id}")
                    }
                )
            }

            composable("post_detail/{postId}") { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")
                PostDetailScreen(
                    postId = postId ?: "",
                    navController = navController,
                )
            }

        }
    }
}
