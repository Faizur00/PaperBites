package com.example.paperbites.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.paperbites.ui.theme.jetbrainsMonoFontFamily
import java.time.LocalDate

@Composable
fun RowScope.YearSelector(year: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(52.dp)
            .clickable(onClick = onClick)
            .border(1.dp, Color(0xFF1A1A1A))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Spacer(Modifier.height(4.dp))
        Text(
            text = year.toString(),
            color = Color(0xFF1A1A1A),
            fontFamily = jetbrainsMonoFontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun YearPickerDialog(
    selectedYear: Int,
    maxYear: Int = LocalDate.now().year,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val years = (maxYear - 100..maxYear).toList().reversed()
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color(0xFF1A1A1A), fontFamily = jetbrainsMonoFontFamily)
            }
        },
        title = {
            Text(
                text = "SELECT YEAR",
                fontFamily = jetbrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
        },
        text = {
            val rowHeight = 48.dp
            val rowSpacing = 8.dp
            val visibleRows = 4
            val totalHeight = (visibleRows - 1) * rowSpacing + visibleRows * rowHeight

            LazyVerticalGrid(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .height(totalHeight),
                columns = GridCells.Adaptive(minSize = 120.dp),
                verticalArrangement = Arrangement.spacedBy(rowSpacing),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(years) { year ->
                    val isSelected = year == selectedYear
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color(0xFF1A1A1A))
                            .background(if (isSelected) Color(0xFF1A1A1A) else Color.Transparent)
                            .clickable { onYearSelected(year) }
                            .height(rowHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = year.toString(),
                            fontFamily = jetbrainsMonoFontFamily,
                            color = if (isSelected) Color.White else Color(0xFF1A1A1A)
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFFF4F2F0),
        shape = RectangleShape,
        modifier = Modifier
            .border(2.dp, Color(0xFF1A1A1A))
    )
}

