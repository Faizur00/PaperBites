package com.example.paperbites.ui.mainfeed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.paperbites.ui.common.SquaredButton
import com.example.paperbites.ui.mainfeed.components.Article
import com.example.paperbites.ui.mainfeed.components.DrawerContent
import com.example.paperbites.ui.mainfeed.components.MainScreenBottomBar
import com.example.paperbites.ui.mainfeed.components.MainScreenTopBar
import com.example.paperbites.ui.theme.BGWhite
import com.example.paperbites.ui.theme.PaperBitesTheme
import com.example.paperbites.ui.theme.jetbrainsMonoFontFamily
import com.example.paperbites.ui.theme.libreBaskervilleFontFamily
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
    val snackbarHostState = remember { SnackbarHostState() }

    val filterSettings by viewModel.filterSettings.collectAsStateWithLifecycle()

    var lastMarkedIndex by rememberSaveable { mutableIntStateOf(0) }

    // Redundant App-start refresh call removed, as Mediator handles INITIAL_REFRESH.
    // LaunchedEffect(Unit) {
    //     pagedPapers.refresh()
    // }

    // Soft Error Toast/Snackbar listener
    LaunchedEffect(Unit) {
        viewModel.softMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

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
                    // Trigger 3: Filter switch triggers refresh
                    viewModel.applyFilters(newSettings)
                    lastMarkedIndex = 0
                    scope.launch { drawerState.close() }
                },
                onReset = {
                    viewModel.resetFilters()
                    lastMarkedIndex = 0
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
                    whiteButtonText = if (isCurrentBookmarked) "SAVED" else "SAVE",
                    doi = currentPaper?.doi
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = BGWhite
        ) { innerPadding ->
            val loadState = pagedPapers.loadState.refresh
            val isRefreshing = loadState is LoadState.Loading

            // Pull gesture directly invokes refreshSession()
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { 
                    lastMarkedIndex = 0
                    viewModel.refreshSession()
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val isEmpty = pagedPapers.itemCount == 0 && (
                    loadState is LoadState.Error ||
                            (loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
                    )

                if (isEmpty) {
                    // Truly unrecoverable state: brand-new filter with zero cached rows, offline
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "NO CONNECTION / NO CONTENT",
                                fontFamily = libreBaskervilleFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF1A1A1A),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No cached papers are available for this filter while offline. Please connect to the internet to load content.",
                                fontFamily = jetbrainsMonoFontFamily,
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            SquaredButton(
                                text = "RETRY REFRESH",
                                onClick = { 
                                    lastMarkedIndex = 0
                                    viewModel.refreshSession()
                                },
                                bgColor = Color(0xFF1A1A1A),
                                textColor = Color.White,
                                icon = Icons.Default.Refresh
                            )
                        }
                    }
                } else {
                    VerticalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        key = { index -> pagedPapers.peek(index)?.id ?: index }
                    ) { page ->
                        pagedPapers[page]?.let { paper ->
                            Article(paper = paper)
                        }
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

