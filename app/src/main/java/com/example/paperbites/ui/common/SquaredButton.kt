package com.example.paperbites.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paperbites.ui.theme.jetbrainsMonoFontFamily

@Composable
fun SquaredButton(
    text: String,
    onClick: () -> Unit,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    borderColor: Color? = null,
    icon: ImageVector? = null,
) {
    Button(
        modifier = modifier
            .size(width = 162.dp, height = 48.dp)
            .defaultMinSize(minHeight = 0.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = textColor
        ),
        shape = RectangleShape,
        border = if (borderColor != null) BorderStroke(1.dp, borderColor) else null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontFamily = jetbrainsMonoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SquaredButtonWhitePreview() {
    SquaredButton(
        text = "SAVE",
        onClick = {},
        bgColor = Color(0xFFF4F2F0),
        textColor = Color(0xFF1A1A1A),
        borderColor = Color(0xFF1A1A1A),
        icon = Icons.Outlined.BookmarkBorder
    )
}

@Preview(showBackground = true)
@Composable
fun SquaredButtonBlackPreview() {
    SquaredButton(
        text = "READ FULL",
        onClick = {},
        bgColor = Color(0xFF1A1A1A),
        textColor = Color(0xFFFDFCFB)
    )
}

