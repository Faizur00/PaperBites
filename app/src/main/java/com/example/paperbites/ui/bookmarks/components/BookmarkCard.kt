//package com.example.paperbites.ui.bookmarks.components
//
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.CloseFullscreen
//import androidx.compose.material.icons.outlined.OpenInFull
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.paperbites.data.database.ArticleData
//import com.example.paperbites.data.database.sampleArticles
//import com.example.paperbites.ui.common.AnimatedIconButton
//import com.example.paperbites.ui.common.rememberFormattedText
//import com.example.paperbites.ui.theme.jetbrainsMonoFontFamily
//import com.example.paperbites.ui.theme.loraFontFamily
//
//@Composable
//fun BookmarkCard(
//    paper: ArticleData,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    isExpanded: Boolean = false
//) {
//    val formattedTitle = rememberFormattedText(paper.title, fontSize = 16.sp) // Material titleMedium is around 16sp
//    val formattedAbstract = rememberFormattedText(paper.abstract, fontSize = 12.sp) // Material bodySmall is around 12sp
//
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .border(1.dp, Color(0xFF1A1A1A)),
//        shape = RectangleShape,
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFCFB)),
//        elevation = CardDefaults.cardElevation(2.dp)
//    ) {
//        Column(Modifier.padding(16.dp)) {
//            Surface(color = Color(0xFFF4F2F0), shape = RectangleShape) {
//                Text(
//                    text = paper.tag,
//                    fontFamily = jetbrainsMonoFontFamily,
//                    fontSize = 10.sp,
//                    fontWeight = FontWeight.Normal,
//                    style = MaterialTheme.typography.labelSmall,
//                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
//                    color = Color(0xFF1A1A1A)
//                )
//            }
//
//            Spacer(Modifier.height(12.dp))
//
//            Text(
//                text = formattedTitle.annotatedString,
//                inlineContent = formattedTitle.inlineContent,
//                fontFamily = loraFontFamily,
//                fontWeight = FontWeight.Bold,
//                style = MaterialTheme.typography.titleMedium,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                color = Color(0xFF1A1A1A)
//            )
//
//            Spacer(Modifier.height(8.dp))
//
//            Text(
//                text = formattedAbstract.annotatedString,
//                inlineContent = formattedAbstract.inlineContent,
//                fontFamily = loraFontFamily,
//                fontWeight = FontWeight.Normal,
//                style = MaterialTheme.typography.bodySmall,
//                color = Color(0xFF8B8987),
//                maxLines = if (!isExpanded) 2 else 6,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            Spacer(Modifier.height(16.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = paper.date.takeLast(4),
//                    fontFamily = jetbrainsMonoFontFamily,
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 10.sp,
//                    color = Color(0xFF1A1A1A)
//                )
//                AnimatedIconButton(
//                    onClick = onClick,
//                    icon = if (isExpanded) Icons.Outlined.CloseFullscreen else Icons.Outlined.OpenInFull,
//                    contentDescription = if (isExpanded) "Open" else "Close",
//                    iconColor = Color(0xFF8B8987),
//                    modifier = Modifier
//                        .size(16.dp)
//                )
//            }
//        }
//    }
//}
//@Preview(showSystemUi = false)
//@Composable
//fun BookmarkCardPreview(){
//    BookmarkCard(
//        paper = sampleArticles[0],
//        onClick = {},
//        isExpanded = true
//    )
//}

