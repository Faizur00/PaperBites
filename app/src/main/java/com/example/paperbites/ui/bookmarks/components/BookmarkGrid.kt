package com.example.paperbites.ui.bookmarks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.paperbites.data.database.Entity.BookmarkEntity

@Composable
fun BookmarkGrid(
    papers: List<BookmarkEntity>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 163.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(17.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            items = papers,
            key = { it.paperId }
        ) { paper ->
            BookmarkCard(
                paper = paper,
                modifier = Modifier
                    .animateItem()
            )
        }
    }
}

