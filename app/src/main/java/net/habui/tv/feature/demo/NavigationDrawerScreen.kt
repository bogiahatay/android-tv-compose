package net.habui.tv.feature.demo

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusEnterExitScope
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Glow
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import net.habui.tv.navigation.Route
import timber.log.Timber

@Composable
fun NavigationDrawerScreen() {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val closeDrawerWidth = 80.dp

    var focusedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val restoreFocusRequester = remember {
        FocusRequester()
    }

    val homeFocusRequester = remember {
        FocusRequester()
    }

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        drawerContent = {

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxHeight()
                    .padding(12.dp)
                    .focusProperties {
                        onEnter = {
                            Timber.tag("TV_FOCUS").d("NavigationDrawer onEnter -> restore focus = $focusedIndex");
                            restoreFocusRequester.requestFocus()
                            FocusRequester.Cancel
                        }
                        onExit = {
                            Timber.tag("TV_FOCUS").d("NavigationDrawer onExit")
                            FocusRequester.Default
                        }
                    }
                    .focusGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                NavigationDrawerItem(
                    selected = focusedIndex == 0,
                    modifier = Modifier.then(
                        if (focusedIndex == 0) {
                            Modifier.focusRequester(restoreFocusRequester)
                        } else {
                            Modifier
                        }
                    ),
                    onClick = {
                        focusedIndex = 0
                        homeFocusRequester.requestFocus()
                    },
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
                    selected = focusedIndex == 1,
                    modifier = Modifier.then(
                        if (focusedIndex == 1) {
                            Modifier.focusRequester(restoreFocusRequester)
                        } else {
                            Modifier
                        }
                    ),
                    onClick = {
                        focusedIndex = 1
                        homeFocusRequester.requestFocus()
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.SportsFootball,
                            contentDescription = null
                        )
                    }
                ) {
                    Text("Sports")
                }

                NavigationDrawerItem(
                    selected = focusedIndex == 2,
                    modifier = Modifier.then(
                        if (focusedIndex == 2) {
                            Modifier.focusRequester(restoreFocusRequester)
                        } else {
                            Modifier
                        }
                    ),

                    onClick = {
                        focusedIndex = 2
                        homeFocusRequester.requestFocus()
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.LibraryMusic,
                            contentDescription = null
                        )
                    }
                ) {
                    Text("Music")
                }

                NavigationDrawerItem(
                    selected = focusedIndex == 3,
                    modifier = Modifier.then(
                        if (focusedIndex == 3) {
                            Modifier.focusRequester(restoreFocusRequester)
                        } else {
                            Modifier
                        }
                    ),
                    onClick = {
                        focusedIndex = 3
                        homeFocusRequester.requestFocus()
                    },
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
        }
    ) {
        HomeScreenGrid(modifier = Modifier
            .padding(start = closeDrawerWidth)
            .background(Color.Gray)
            .fillMaxSize()
            .focusRequester(homeFocusRequester)
        )
    }
}

@SuppressLint("LogNotTimber")
@Composable
fun HomeScreenGrid(modifier: Modifier = Modifier) {

    val items = remember {
        (0 until 25).toList()
    }

    var focusedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val restoreFocusRequester = remember {
        FocusRequester()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .focusProperties {
                onEnter = {
                    Timber.tag("TV_FOCUS").d("HomeScreenGrid onEnter -> restore focus = $focusedIndex");
                    restoreFocusRequester.requestFocus()
                    FocusRequester.Default
                }
                onExit = {
                    Timber.tag("TV_FOCUS").d("HomeScreenGrid onExit");
                    FocusRequester.Default
                }
            }
            .focusGroup()
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            items(
                items = items,
                key = { it }
            ) { index ->

                val isFocusedItem = index == focusedIndex
                Card(
                    onClick = {},
                    scale = CardDefaults.scale(
                        focusedScale = 1.1f
                    ),
                    border = CardDefaults.border(
                        focusedBorder = Border(
                            border = BorderStroke(0.dp, Color.Red)
                        )
                    ),
                    glow = CardDefaults.glow(
                        focusedGlow = Glow(
                            elevation = 8.dp,
                            elevationColor = Color.Red
                        )
                    ),
                    modifier = Modifier
                        .then(
                            if (isFocusedItem) {
                                Modifier.focusRequester(restoreFocusRequester)
                            } else {
                                Modifier
                            }
                        )
                        .onFocusChanged {
                            if (it.isFocused) {
                                focusedIndex = index
                                Timber.tag("TV_FOCUS").d("HomeScreenGrid focused item = $index");
                            }
                        }
                        .aspectRatio(16f / 9f)
                ) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Item $index"
                        )
                    }
                }
            }
        }
    }
}
