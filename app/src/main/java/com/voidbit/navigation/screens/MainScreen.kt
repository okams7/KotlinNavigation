package com.voidbit.navigation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.voidbit.navigation.R
import com.voidbit.navigation.nav.AppNavGraph
import com.voidbit.navigation.nav.RootScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentSelectedScreen by navController.currentScreenAsState()
    val currentRoute by navController.currentRouteAsState()
    /***
     * Define a list of routes if you wanna show it for specific routes.
     * For example I wanna show the app bar only for these routes
    val bottomNavRoutes = listOf(
    LeafScreen.Home.route,
    LeafScreen.Search.route,
    LeafScreen.Favorites.route,
    LeafScreen.Profile.route,
    )
     ***/
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentSelectedScreen = currentSelectedScreen)
            /***
             * Use [BottomNavBar] Like this if you wanna show it for specific routes
            if (currentRoute == null || bottomNavRoutes.contains(currentRoute)) {
            BottomNavBar(navController = navController, currentSelectedScreen = currentSelectedScreen)
            }
             ***/
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AppNavGraph(navController = navController)
        }
    }
}

@Composable
private fun BottomNavBar(
    navController: NavController,
    currentSelectedScreen: RootScreen
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Home,
            onClick = { navController.navigateToRootScreen(RootScreen.Home) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.home))
            },
            icon = {
                Icon(
                    Icons.Rounded.Home,
                    contentDescription = ""
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Search,
            onClick = { navController.navigateToRootScreen(RootScreen.Search) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.search))
            },
            icon = {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = ""
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Favorites,
            onClick = { navController.navigateToRootScreen(RootScreen.Favorites) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.favorites))
            },
            icon = {
                Icon(
                    Icons.Rounded.Favorite,
                    contentDescription = ""
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Profile,
            onClick = { navController.navigateToRootScreen(RootScreen.Profile) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.profile))
            },
            icon = {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = ""
                )
            }
        )
    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Home) }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == RootScreen.Home.route } -> {
                    selectedItem.value = RootScreen.Home
                }
                destination.hierarchy.any { it.route == RootScreen.Search.route } -> {
                    selectedItem.value = RootScreen.Search
                }
                destination.hierarchy.any { it.route == RootScreen.Favorites.route } -> {
                    selectedItem.value = RootScreen.Favorites
                }
                destination.hierarchy.any { it.route == RootScreen.Profile.route } -> {
                    selectedItem.value = RootScreen.Profile
                }
            }

        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}

@Stable
@Composable
private fun NavController.currentRouteAsState(): State<String?> {
    val selectedItem = remember { mutableStateOf<String?>(null) }
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem.value = destination.route
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}

private fun NavController.navigateToRootScreen(rootScreen: RootScreen) {
    navigate(rootScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}