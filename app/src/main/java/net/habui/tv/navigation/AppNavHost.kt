package net.habui.tv.navigation

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.tv.material3.NavigationDrawerItemDefaults
import androidx.tv.material3.NavigationDrawerScope
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import net.habui.tv.feature.home.presentation.HomeScreen
import net.habui.tv.feature.home.presentation.HomeViewModel
import net.habui.tv.feature.player.presentation.PlayerScreen
import net.habui.tv.feature.settings.presentation.SettingsScreen
import net.habui.tv.feature.settings.presentation.SettingsViewModel

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
    settingsViewModel: SettingsViewModel
) {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    val focusManager = LocalFocusManager.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val homeDrawerFocusRequester = remember { FocusRequester() }

    val settingsDrawerFocusRequester = remember { FocusRequester() }

    val drawerGradientWidthPx = with(LocalDensity.current) {
        DrawerGradientWidth.toPx()
    }

    var homeFocusRestoreRequest by remember {
        mutableIntStateOf(0)
    }

    var focusedDrawerRoute by remember {
        mutableStateOf<String?>(null)
    }

    val topLevelRoutes = remember {
        listOf(
            Route.Home.route,
            Route.Settings.route
        )
    }

    val showDrawer = currentRoute in topLevelRoutes

    fun navigateTopLevel(route: String) {

        if (currentRoute == route) return

        navController.navigate(route) {

            popUpTo(
                navController.graph.findStartDestination().id
            ) {
                saveState = true
            }

            launchSingleTop = true

            restoreState = true
        }
    }

    fun closeDrawerAndRestoreContentFocus() {

        drawerState.setValue(DrawerValue.Closed)

        if (currentRoute == Route.Home.route) {
            homeFocusRestoreRequest++
        } else {
            focusManager.moveFocus(FocusDirection.Right)
        }
    }

    LaunchedEffect(currentRoute) {

        if (!showDrawer) {

            drawerState.setValue(DrawerValue.Closed)

            focusedDrawerRoute = null
        }
    }

    LaunchedEffect(drawerState.currentValue) {

        if (drawerState.currentValue == DrawerValue.Open) {

            when (currentRoute) {

                Route.Home.route -> {
                    homeDrawerFocusRequester.requestFocus()
                }

                Route.Settings.route -> {
                    settingsDrawerFocusRequester.requestFocus()
                }
            }
        }
    }

    if (showDrawer) {

        val isDrawerExpanded =
            drawerState.currentValue == DrawerValue.Open

        val highlightedDrawerRoute =
            if (isDrawerExpanded) {
                focusedDrawerRoute ?: currentRoute
            } else {
                currentRoute
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

                    DrawerItem(
                        selected = highlightedDrawerRoute == Route.Home.route,
                        icon = Icons.Default.Home,
                        label = "Trang chủ",
                        focusRequester = homeDrawerFocusRequester,
                        onFocused = {
                            focusedDrawerRoute = Route.Home.route
                        },
                        onClick = {

                            if (currentRoute == Route.Home.route) {

                                closeDrawerAndRestoreContentFocus()

                            } else {

                                navigateTopLevel(Route.Home.route)
                            }
                        }
                    )

                    DrawerItem(
                        selected = highlightedDrawerRoute == Route.Settings.route,
                        icon = Icons.Default.Settings,
                        label = "Cài đặt",
                        focusRequester = settingsDrawerFocusRequester,
                        onFocused = {
                            focusedDrawerRoute = Route.Settings.route
                        },
                        onClick = {

                            if (currentRoute == Route.Settings.route) {

                                closeDrawerAndRestoreContentFocus()

                            } else {

                                navigateTopLevel(Route.Settings.route)
                            }
                        }
                    )
                }
            },
            scrimBrush = Brush.horizontalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.scrim,
                    Color.Transparent
                ),
                startX = 0f,
                endX = drawerGradientWidthPx
            )
        ) {

            Box(
                modifier = Modifier
                    .padding(start = CollapsedDrawerWidth)
                    .fillMaxSize()
            ) {

                NavContent(
                    navController = navController,
                    homeFocusRestoreRequest = homeFocusRestoreRequest,
                    settingsViewModel = settingsViewModel
                )
            }
        }

    } else {

        NavContent(
            navController = navController,
            homeFocusRestoreRequest = homeFocusRestoreRequest,
            settingsViewModel = settingsViewModel
        )
    }
}

@Composable
private fun NavigationDrawerScope.DrawerItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    focusRequester: FocusRequester,
    onFocused: () -> Unit,
    onClick: () -> Unit,
) {

    NavigationDrawerItem(
        selected = selected,
        onClick = onClick,
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged {

                if (it.isFocused) {
                    onFocused()
                }
            },
        leadingContent = {

            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
            focusedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            pressedContainerColor = MaterialTheme.colorScheme.inverseSurface,
            pressedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedSelectedContainerColor = MaterialTheme.colorScheme.inverseSurface,
            focusedSelectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            pressedSelectedContainerColor = MaterialTheme.colorScheme.inverseSurface,
            pressedSelectedContentColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    ) {

        Text(text = label)
    }
}

@Composable
private fun NavContent(
    navController: androidx.navigation.NavHostController,
    homeFocusRestoreRequest: Int,
    settingsViewModel: SettingsViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Route.Home.route,
        modifier = Modifier.fillMaxSize()
    ) {

        composable(Route.Home.route) {

            val viewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                viewModel = viewModel,
                focusRestoreRequest = homeFocusRestoreRequest,
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
                navArgument("contentId") {
                    type = NavType.StringType
                },
                navArgument("playbackType") {
                    type = NavType.StringType
                },
                navArgument("title") {
                    type = NavType.StringType
                },
                navArgument("streamUrl") {
                    type = NavType.StringType
                }
            )
        ) {

            PlayerScreen()
        }

        composable(Route.Settings.route) {

            SettingsScreen(viewModel = settingsViewModel)
        }
    }
}
