//package com.example.paperbites.ui.bookmarks.components
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.GridItemSpan
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.paperbites.data.database.ArticleData
//
//@Composable
//fun BookmarkGrid(
//    papers: List<ArticleData>,
//    onToggle: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    LazyVerticalGrid(
//        columns = GridCells.Adaptive(minSize = 163.dp),
//        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
//        horizontalArrangement = Arrangement.spacedBy(17.dp),
//        verticalArrangement = Arrangement.spacedBy(18.dp),
//        modifier = modifier
//            .fillMaxSize()
//    ) {
//        items(
//            items = papers,
//            key = { it.id },
//            span = { paper -> GridItemSpan(if (paper.isExpanded) 2 else 1) }
//        ) { paper ->
//            BookmarkCard(
//                paper = paper,
//                isExpanded = paper.isExpanded,
//                onClick = { onToggle(paper.id.toString()) },
//                modifier = Modifier
//                    .animateItem()
//
//            )
//        }
//    }
//}

