package com.example.paperbites.ui.bookmarks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paperbites.ui.common.AnimatedIconButton
import com.example.paperbites.ui.theme.libreBaskervilleFontFamily

@Composable
fun BookmarkTopBar(
    onBack: () -> Unit
){
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .height(68.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedIconButton(
            icon = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "back",
            onClick = onBack,
            iconColor = Color(0xFF1A1A1A),
            modifier = Modifier
        )

        Text(
            text = "Archive",
            color = Color(0xFF1A1A1A),
            fontFamily = libreBaskervilleFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier
        )

        AnimatedIconButton(
            icon = Icons.Outlined.Bookmarks,
            contentDescription = "bookmarks",
            onClick = {},
            iconColor = Color(0x001A1A1A),
            modifier = Modifier
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun BookmarkPreview(){
    BookmarkTopBar(onBack = {})
}

