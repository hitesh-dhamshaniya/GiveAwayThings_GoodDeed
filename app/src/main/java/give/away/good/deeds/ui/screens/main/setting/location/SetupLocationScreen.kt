package give.away.good.deeds.ui.screens.main.setting.location

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import give.away.good.deeds.ui.theme.AppThemeButtonShape
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupLocationScreen(
    onBackPress: () -> Unit,
) {
    Scaffold(topBar = {
        MediumTopAppBar(
            title = {
                Text(
                    text = "Setup Location",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Arrow"
                    )
                }
            },
        )
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            LocationScreenView(onBackPress)
        }
    }
}

@Composable
fun LocationScreenView(
    onBackPress: () -> Unit,
    viewModel: SetupLocationViewModel = koinViewModel()
) {

    LaunchedEffect(Unit, block = {
        viewModel.getLocation()
    })

    val latLng by viewModel.uiState.collectAsState()
    if (latLng == null) {
        LoadingView()
    } else {
        GoogleMapView(
            defaultLatLng = latLng ?: viewModel.defaultLatLng,
            onBackPress = onBackPress
        )
    }

}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun GoogleMapView(
    defaultLatLng: LatLng,
    onBackPress: () -> Unit,
    viewModel: SetupLocationViewModel = koinViewModel()
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 10f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp, 16.dp, 16.dp),
    ) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                viewModel.setTempLocation(latLng)
            }
        ) {
            Marker(
                state = MarkerState(position = defaultLatLng),
                snippet = "Selected location",
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = AppThemeButtonShape,
            onClick = {
                viewModel.setLocation()
                Toast.makeText(context, "Location saved successfully!", Toast.LENGTH_SHORT).show()
                onBackPress()
            },
        ) {
            Text(
                text = "Save".uppercase(),
                modifier = Modifier.padding(8.dp),
            )
        }

    }

}