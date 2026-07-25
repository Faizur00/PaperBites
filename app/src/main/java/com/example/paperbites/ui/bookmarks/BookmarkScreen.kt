package com.example.paperbites.ui.bookmarks

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paperbites.ui.bookmarks.components.BookmarkGrid
import com.example.paperbites.ui.bookmarks.components.BookmarkTopBar
import com.example.paperbites.ui.viewmodel.AppViewModelProvider
import com.example.paperbites.ui.viewmodel.BookmarkViewModel


@Composable
fun BookmarkScreen(
    onBack: () -> Unit,
    viewModel: BookmarkViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val bookmarkUiState by viewModel.bookmarkUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BookmarkTopBar(onBack = onBack)
        },
        bottomBar = {
            null
        }
    ) { innerPadding ->
        Surface(
            color = Color(0xFFFDFCFB),
        ) {
            BookmarkGrid(
                modifier = Modifier.padding(innerPadding),
                papers = bookmarkUiState
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookmarkScreenPreview(){
    BookmarkScreen(onBack = {})
}

