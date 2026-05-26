package net.habui.tv.feature.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NavigationDrawerScreen() {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val closeDrawerWidth = 80.dp
    val drawerWidthPx = with(LocalDensity.current) {
        400.dp.toPx()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,

        drawerContent = {

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxHeight()
                    .padding(12.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                NavigationDrawerItem(
                    selected = true,
                    onClick = { },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null
                        )
                    }
                ) {
                    Text("Home")
                }

                NavigationDrawerItem(
                    selected = false,
                    onClick = { },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null
                        )
                    }
                ) {
                    Text("Settings")
                }
            }
        },
        scrimBrush = Brush.horizontalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.scrim,
                Color.Transparent
            ),
            startX = 0f,
            endX = drawerWidthPx
        )
    ) {

        Box(
            modifier = Modifier
                .padding(closeDrawerWidth)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Button(
                onClick = {
                    scope.launch {

                        if (drawerState.currentValue == DrawerValue.Closed) {
                            drawerState.setValue(DrawerValue.Open)
                        } else {
                            drawerState.setValue(DrawerValue.Closed)
                        }
                    }
                }
            ) {
                Text("Toggle Drawer")
            }
        }
    }
}
