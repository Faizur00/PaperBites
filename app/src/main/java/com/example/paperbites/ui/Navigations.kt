package com.example.paperbites.ui

import androidx.compose.runtime.Composable
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
        navController = navController,
        startDestination = Feed
    ) {
        composable<Feed> {
            MainFeedScreen(
                onOpenBookmarks = { navController.navigate(Bookmarks) }
            )
        }
        composable<Bookmarks> {
            BookmarkScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
