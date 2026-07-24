package com.example.paperbites.ui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paperbites.ui.theme.jetbrainsMonoFontFamily
import java.util.Locale

@Composable
fun CustomToggleBox(
    label: String,
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) Color(0xFF1A1A1A) else Color(0xFFE5E3E0),
        label = "chipContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color(0xFF1A1A1A),
        label = "chipContent",
    )

    Box(
        modifier = modifier
            .height(52.dp)
            .background(containerColor)
            .toggleable(
                value = selected,
                role = Role.Checkbox,
                onValueChange = {
                    onToggle()
                },
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
            ,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            color = contentColor,
            fontSize = 11.sp,
            fontFamily = jetbrainsMonoFontFamily,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2
        )
    }
}




@Preview
@Composable
fun CustomToggleBoxPreview() {
    CustomToggleBox(label = "Label", selected = true, onToggle = {})
}
