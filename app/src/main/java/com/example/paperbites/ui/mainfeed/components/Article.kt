package com.example.paperbites.ui.mainfeed.components

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paperbites.data.database.Entity.PaperEntity
import com.example.paperbites.ui.common.rememberFormattedText
import com.example.paperbites.ui.theme.BGWhite
import com.example.paperbites.ui.theme.jetbrainsMonoFontFamily
import com.example.paperbites.ui.theme.libreBaskervilleFontFamily
import com.example.paperbites.ui.theme.loraFontFamily
import kotlinx.coroutines.launch

@Composable
fun Article(paper: PaperEntity){
    Column(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ){
        ArticleHeader(
            tag = paper.primaryTopicName ?: paper.fieldName ?: "GENERAL",
            date = paper.publicationYear?.toString() ?: "N/A",
            isBookmarked = paper.bookmarked
        )
        ArticleTitle(paper.title)
        ArticleAuthor(paper.authorsDisplay)
        ArticleAbstract(paper.abstract)
    }
}


@Composable
fun ArticleHeader(tag: String, date: String, isBookmarked: Boolean = false){
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 18.dp),
    ) {
        Box(
            modifier = Modifier
                .background(color = Color(0xFFF4F2F0))
        ){
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                text = tag,
                fontFamily = jetbrainsMonoFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .background(color = Color(0x00F4F2F0))
        ){
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                text = date,
                color = Color(0xFF8B8B8B),
                fontFamily = jetbrainsMonoFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal
            )
        }

        if (isBookmarked) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.Bookmark,
                contentDescription = "Bookmarked",
                tint = Color(0xFF8B8B8B),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ArticleTitle(title: String){
    val formattedTitle = rememberFormattedText(title, fontSize = 24.sp)
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(bottom = 4.dp)
    ){
        Text(
            text = formattedTitle.annotatedString,
            inlineContent = formattedTitle.inlineContent,
            fontFamily = libreBaskervilleFontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 35.sp
        )
    }
}

@Composable
fun ArticleAuthor(authors: String){
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(bottom = 18.dp)
    ){
        Text(
            text = authors,
            fontFamily = loraFontFamily,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF8B8987),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ArticleAbstract(abstract: String){
    val formattedAbstract = rememberFormattedText(abstract, fontSize = 18.sp)
    var hasVisualOverflow by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
    ){
        Text(
            text = formattedAbstract.annotatedString,
            inlineContent = formattedAbstract.inlineContent,
            fontFamily = loraFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF222222),
            lineHeight = 28.sp,
            overflow = TextOverflow.Clip,
            onTextLayout = { textLayoutResult ->
                hasVisualOverflow = textLayoutResult.hasVisualOverflow
            }
        )

        if (hasVisualOverflow) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                BGWhite.copy(alpha = 0f),
                                BGWhite.copy(alpha = 0.5f),
                                BGWhite
                            ),
                            startY = 300f
                        )
                    )
            )
            // Read more Button
            val scope = rememberCoroutineScope()
            val scale = remember { Animatable(1f) }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = Color.Transparent)
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ){
                        scope.launch {
                            scale.animateTo(
                                targetValue = 0.8f,
                                animationSpec = tween(durationMillis = 100)
                            )
                            scale.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 100)
                            )
                        }
                        // TODO: Add the launcher thing from the bottom for full abstract
                    }
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 18.dp, vertical = 9.dp),
                    text = "READ FULL ABSTRACT",
                    fontFamily = jetbrainsMonoFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ArticlePreview(){
    Article(
        paper = PaperEntity(
            id = "W1",
            doi = "10.1234/5678",
            title = "Attention Is All You Need: \$E = mc^2\$",
            abstract = "The dominant sequence transduction models are based on complex recurrent or convolutional neural networks. We propose a new simple network architecture, the Transformer, based solely on attention mechanisms. For a given sequence \\( \\mathbf{x} = (x_1, \\dots, x_n) \\), the model computes \$ \\text{Attention}(Q, K, V) = \\text{softmax}\\left(\\frac{QK^T}{\\sqrt{d_k}}\\right)V \$.",
            authorsDisplay = "Ashish Vaswani, Noam Shazeer, Niki Parmar",
            venueName = "NeurIPS",
            publicationYear = 2017,
            domainName = "Computer Science",
            fieldName = "Artificial Intelligence",
            primaryTopicName = "Large Language Models",
            language = "en"
        )
    )
}

