package net.habui.tv.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ModalNavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import net.habui.tv.feature.home.presentation.HomeScreen
import net.habui.tv.feature.home.presentation.HomeViewModel
import net.habui.tv.feature.player.presentation.PlayerScreen
import net.habui.tv.feature.settings.presentation.SettingsScreen
import net.habui.tv.feature.settings.presentation.SettingsViewModel
import timber.log.Timber

private val CollapsedDrawerWidth = 80.dp
private val DrawerGradientWidth = 360.dp

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AppNavHost() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    AppNavHost(settingsViewModel = settingsViewModel)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AppNavHost(
    settingsViewModel: SettingsViewModel,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerGradientWidthPx = with(LocalDensity.current) { DrawerGradientWidth.toPx() }

    var focusedIndex by rememberSaveable { mutableIntStateOf(0) }
    val restoreFocusRequester = remember { FocusRequester() }
    val contentFocusRequester = remember { FocusRequester() }

    val topLevelRoutes = remember { listOf(Route.Home.route, Route.Settings.route) }
    val showDrawer = currentRoute in topLevelRoutes

    fun navigateTopLevel(route: String) {
        if (currentRoute == route) return
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    LaunchedEffect(showDrawer) {
        if (!showDrawer) drawerState.setValue(DrawerValue.Closed)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (showDrawer) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxHeight()
                        .padding(12.dp)
                        .focusProperties {
                            onEnter = {
                                Timber.tag("TV_FOCUS").d("NavigationDrawer onEnter -> restore focus = $focusedIndex")
                                restoreFocusRequester.requestFocus()
                                FocusRequester.Cancel
                            }
                            onExit = {
                                Timber.tag("TV_FOCUS").d("NavigationDrawer onExit")
                                FocusRequester.Default
                            }
                        }
                        .focusGroup()
                ) {
                    NavigationDrawerItem(
                        selected = focusedIndex == 0,
                        modifier = Modifier.then(
                            if (focusedIndex == 0) Modifier.focusRequester(restoreFocusRequester)
                            else Modifier
                        ),
                        onClick = {
                            focusedIndex = 0
                            drawerState.setValue(DrawerValue.Closed)
                            if (currentRoute == Route.Home.route) {
                                contentFocusRequester.requestFocus()
                            } else {
                                navigateTopLevel(Route.Home.route)
                            }
                        },
                        leadingContent = {
                            Icon(imageVector = Icons.Default.Home, contentDescription = null)
                        }
                    ) {
                        Text("Home")
                    }
                    NavigationDrawerItem(
                        selected = focusedIndex == 2,
                        modifier = Modifier.then(
                            if (focusedIndex == 2) Modifier.focusRequester(restoreFocusRequester)
                            else Modifier
                        ),
                        onClick = {
                            focusedIndex = 2
                            drawerState.setValue(DrawerValue.Closed)
                            if (currentRoute == Route.Settings.route) {
                                contentFocusRequester.requestFocus()
                            } else {
                                navigateTopLevel(Route.Settings.route)
                            }
                        },
                        leadingContent = {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                        }
                    ) {
                        Text("Cài Đặt")
                    }
                }
            }
        },
        scrimBrush = Brush.horizontalGradient(
            colors = listOf(MaterialTheme.colorScheme.scrim, Color.Transparent),
            startX = 0f,
            endX = drawerGradientWidthPx
        )
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.Home.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(contentFocusRequester)
        ) {
            composable(Route.Home.route) {
                val viewModel: HomeViewModel = hiltViewModel()

                HomeScreen(
                    modifier = Modifier.padding(start = CollapsedDrawerWidth),
                    viewModel = viewModel,
                    onMovieClick = { movie ->
                        navController.navigate(
                            Route.Player.createRoute(
                                contentId = movie.id,
                                playbackType = movie.playbackType.name,
                                title = movie.title,
                                streamUrl = movie.videoUrl
                            )
                        )
                    }
                )
            }

            composable(
                route = Route.Player.route,
                arguments = listOf(
                    navArgument("contentId") { type = NavType.StringType },
                    navArgument("playbackType") { type = NavType.StringType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("streamUrl") { type = NavType.StringType }
                )
            ) {
                PlayerScreen()
            }

            composable(Route.Settings.route) {
                Box(
                    modifier = Modifier
                        .padding(start = CollapsedDrawerWidth)
                        .fillMaxSize()
                ) {
                    SettingsScreen(viewModel = settingsViewModel)
                }
            }
        }
    }
}
