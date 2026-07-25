package com.example.paperbites.ui.mainfeed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.paperbites.ui.mainfeed.components.Article
import com.example.paperbites.ui.mainfeed.components.DrawerContent
import com.example.paperbites.ui.mainfeed.components.MainScreenBottomBar
import com.example.paperbites.ui.mainfeed.components.MainScreenTopBar
import com.example.paperbites.ui.theme.BGWhite
import com.example.paperbites.ui.theme.PaperBitesTheme
import com.example.paperbites.ui.viewmodel.AppViewModelProvider
import com.example.paperbites.ui.viewmodel.MainFeedViewModel
import kotlinx.coroutines.launch

private const val SERVED_BATCH_SIZE = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFeedScreen(
    onOpenBookmarks: () -> Unit,
    viewModel: MainFeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val pagedPapers = viewModel.pagedPapers.collectAsLazyPagingItems()

    val pagerState = rememberPagerState(pageCount = {
        pagedPapers.itemCount
    })
    
    val currentPaper = if (pagerState.currentPage < pagedPapers.itemCount) {
        pagedPapers[pagerState.currentPage]
    } else null

    val isCurrentBookmarked = currentPaper?.bookmarked ?: false
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val filterSettings by viewModel.filterSettings.collectAsStateWithLifecycle()

    var lastMarkedIndex by rememberSaveable { mutableIntStateOf(0) }

    // Scroll tracking: only mark papers as served when scrolling meaningfully forward past lastMarkedIndex
    LaunchedEffect(pagerState.currentPage) {
        val currentlyOnIndex = pagerState.currentPage
        if (currentlyOnIndex >= (lastMarkedIndex + SERVED_BATCH_SIZE)) {
            val newIds = mutableListOf<String>()
            for (i in lastMarkedIndex until currentlyOnIndex) {
                pagedPapers.peek(i)?.id?.let { newIds.add(it) }
            }
            if (newIds.isNotEmpty()) {
                viewModel.markAsServed(newIds)
            }
            lastMarkedIndex = currentlyOnIndex
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,

        drawerContent = {
            DrawerContent(
                initialSettings = filterSettings,
                onApply = { newSettings ->
                    viewModel.applyFilters(newSettings)
                    scope.launch { drawerState.close() }
                },
                onReset = {
                    viewModel.resetFilters()
                    scope.launch { drawerState.close() }
                },
                onCloseButton = {
                    scope.launch {
                        if (drawerState.isOpen) drawerState.close()
                    }
                }
            )
        },
    ) {
        Scaffold(
            topBar = {
                MainScreenTopBar(
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (drawerState.isClosed) drawerState.open()
                            }
                        }
                    },
                    onOpenBookmarks = onOpenBookmarks
                )
            },
            bottomBar = {
                MainScreenBottomBar(
                    onClick = {
                        currentPaper?.let { viewModel.toggleBookmark(it) }
                    },
                    whiteButtonIcon = if (isCurrentBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    whiteButtonText = if (isCurrentBookmarked) "SAVED" else "SAVE"
                )
            },
            containerColor = BGWhite
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                VerticalPager(
                    state = pagerState,
                ) { page ->
                    pagedPapers[page]?.let { paper ->
                        Article(paper = paper)
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true, device = Devices.PHONE)
@Composable
fun MainFeedScreenPreview() {
    PaperBitesTheme {
        MainFeedScreen(onOpenBookmarks = {})
    }
}

