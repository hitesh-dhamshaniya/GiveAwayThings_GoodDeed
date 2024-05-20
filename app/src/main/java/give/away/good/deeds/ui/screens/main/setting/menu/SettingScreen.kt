package give.away.good.deeds.ui.screens.main.setting.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import give.away.good.deeds.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingUtil.getSettingItems().forEach { model ->
                item {
                    SettingsListItem(model, onNavigate)
                }
            }
        }
    }
}

@Composable
fun SettingsListItem(
    model: SettingItemModel,
    onNavigate: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNavigate(model.route)
            },
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painterResource(model.icon),
                model.title,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                model.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )

            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Back Arrow"
            )
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(
            onNavigate = {

            }
        )
    }
}