package com.example.paperbites.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.paperbites.ui.bookmarks.BookmarkScreen
import com.example.paperbites.ui.mainfeed.MainFeedScreen
import kotlinx.serialization.Serializable

@Serializable
object Feed

@Serializable
object Bookmarks

@Composable
fun PaperBitesNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        enterTransition = {
            if (initialState.destination.hasRoute<Feed>() && targetState.destination.hasRoute<Bookmarks>()) {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start)
            } else {
                EnterTransition.None
            }
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { it })
        },
        popEnterTransition = {
            if (initialState.destination.hasRoute<Bookmarks>() && targetState.destination.hasRoute<Feed>()) {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End)
            } else {
                EnterTransition.None
            }
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it })
        },
        navController = navController,
        startDestination = Feed
    ) {
        composable<Feed> {
            MainFeedScreen(onOpenBookmarks = { navController.navigate(Bookmarks) })
        }
        composable<Bookmarks> {
            BookmarkScreen(onBack = { navController.popBackStack() })
        }
    }
}
