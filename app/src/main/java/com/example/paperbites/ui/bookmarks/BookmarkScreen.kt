//package com.example.paperbites.ui.bookmarks
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.paperbites.data.database.sampleArticles
//import com.example.paperbites.ui.bookmarks.components.BookmarkGrid
//import com.example.paperbites.ui.bookmarks.components.BookmarkTopBar
//
//
//@Composable
//fun BookmarkScreen(){
//    Scaffold(
//        topBar = {
//            BookmarkTopBar()
//        },
//        bottomBar = {
//            null
//        }
//    ) { innerPadding ->
//        var papers by remember { mutableStateOf(sampleArticles) }
//        Surface(
//            color = Color(0xFFFDFCFB),
//        ) {
//            BookmarkGrid(
//                modifier = Modifier.padding(innerPadding),
//                papers = papers,
//                onToggle = { id ->
//                    papers =
//                        papers.map { if (it.id.toString() == id) it.copy(isExpanded = !it.isExpanded) else it }
//                }
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun BookmarkScreenPreview(){
//    BookmarkScreen()
//}

