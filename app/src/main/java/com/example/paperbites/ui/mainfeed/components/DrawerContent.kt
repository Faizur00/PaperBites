package com.example.paperbites.ui.mainfeed.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paperbites.data.model.availableTopicFields
import com.example.paperbites.datastore.FilterSettings
import com.example.paperbites.ui.common.AnimatedIconButton
import com.example.paperbites.ui.common.CustomToggleBox
import com.example.paperbites.ui.common.SquaredButton
import com.example.paperbites.ui.common.YearPickerDialog
import com.example.paperbites.ui.common.YearSelector
import com.example.paperbites.ui.theme.jetbrainsMonoFontFamily
import com.example.paperbites.ui.theme.libreBaskervilleFontFamily
import java.time.LocalDate

@Composable
fun DrawerContent(
    initialSettings: FilterSettings,
    onApply: (FilterSettings) -> Unit,
    onReset: () -> Unit,
    onCloseButton: () -> Unit
){
    var draftSettings by remember(initialSettings) { mutableStateOf(initialSettings) }

    Surface(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight(),
        color = Color(0xFFF4F2F0)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color(0xFF1A1A1A))
                .padding(top = 24.dp)
                ,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            DrawerHeader(
                onCloseButton = onCloseButton
            )

            HorizontalDivider(
                color = Color(0xFF1A1A1A),
                thickness = 1.dp,
                modifier = Modifier
            )

            Spacer(Modifier.height(24.dp))

            DrawerRangeFilter(
                fromYear = draftSettings.fromYear,
                toYear = draftSettings.toYear,
                onRangeChanged = { from, to ->
                    draftSettings = draftSettings.copy(fromYear = from, toYear = to)
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 18.dp),
                color = Color(0xFFE5E3E0)
            )
            TopicFilterSection(
                selectedFieldId = draftSettings.fieldId,
                selectedSubfieldIds = draftSettings.subfieldIds,
                onSettingsChanged = { field, subfields ->
                    draftSettings = draftSettings.copy(fieldId = field, subfieldIds = subfields)
                },
                modifier = Modifier
            )


            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                color = Color(0xFF1A1A1A),
                thickness = 1.dp
            )

            DrawerLowerSection(
                onApply = { onApply(draftSettings) },
                onReset = onReset
            )
        }
    }
}

@Composable
fun DrawerHeader(
    onCloseButton: () -> Unit
){
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "SEARCH FILTERS",
            color = Color(0xFF1A1A1A),
            fontFamily = libreBaskervilleFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            letterSpacing = 0.5.sp
        )
        AnimatedIconButton(
            icon = Icons.Outlined.Close,
            iconColor = Color(0xFF1A1A1A),
            contentDescription = "Close",
            onClick = {
                onCloseButton()
            },
            modifier = Modifier
        )
    }
}

@Composable
fun DrawerRangeFilter(
    fromYear: Int,
    toYear: Int,
    onRangeChanged: (Int, Int) -> Unit
) {
    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = "PUBLICATION PERIOD",
            color = Color(0xFF1A1A1A),
            fontFamily = jetbrainsMonoFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

       Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
           YearSelector(year = fromYear) { showFromPicker = true }
           HorizontalDivider(
               color = Color(0xFF1A1A1A),
               thickness = 1.dp,
               modifier = Modifier
                   .width(10.dp)
           )
           YearSelector(year = toYear) { showToPicker = true }
        }
    }

    if (showFromPicker) {
        YearPickerDialog(
            selectedYear = fromYear,
            maxYear = toYear,
            onYearSelected = { onRangeChanged(it, toYear); showFromPicker = false },
            onDismiss = { showFromPicker = false }
        )
    }

    if (showToPicker) {
        YearPickerDialog(
            selectedYear = toYear,
            maxYear = LocalDate.now().year,
            onYearSelected = { onRangeChanged(fromYear, it); showToPicker = false },
            onDismiss = { showToPicker = false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopicFilterSection(
    selectedFieldId: String,
    selectedSubfieldIds: Set<String>,
    onSettingsChanged: (String, Set<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedField = availableTopicFields.find { it.id == selectedFieldId } ?: availableTopicFields.first()

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "FIELD",
                fontFamily = jetbrainsMonoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Color(0xFF8B8987),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, Color(0xFF1A1A1A))
                    .clickable { 
                        // Implementation for switching fields if more fields are added.
                    }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedField.displayName.uppercase(),
                        fontFamily = jetbrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Field",
                        tint = Color(0xFF1A1A1A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SUBFIELDS",
                fontFamily = jetbrainsMonoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Color(0xFF8B8987),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            selectedField.subfields.forEach { subfield ->
                val isSelected = subfield.displayName in selectedSubfieldIds
                CustomToggleBox(
                    label = subfield.displayName,
                    selected = isSelected,
                    onToggle = {
                        val newSubfields = if (isSelected) {
                            selectedSubfieldIds - subfield.displayName
                        } else {
                            selectedSubfieldIds + subfield.displayName
                        }
                        onSettingsChanged(selectedFieldId, newSubfields)
                    },
                    modifier = Modifier.width(140.dp)
                )
            }
        }
    }
}


@Composable
fun DrawerLowerSection(
    onApply: () -> Unit,
    onReset: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        SquaredButton(
            text = "RESET",
            onClick = onReset,
            bgColor = Color(0x00FFFFFF),
            textColor = Color(0xFF1A1A1A),
            modifier = Modifier
                .width(140.dp)
                .height(50.dp)
                .border(1.dp, color = Color(0xFF1A1A1A))
        )

        SquaredButton(
            text = "APPLY TUNINGS",
            onClick = onApply,
            bgColor = Color(0xFF1A1A1A),
            textColor = Color.White,
            icon = Icons.Default.Sync,
            modifier = Modifier
                .width(140.dp)
                .height(50.dp)
        )

    }
}
@Preview(showBackground = true)
@Composable
fun DrawerContentPreview(){
    DrawerContent(
        initialSettings = FilterSettings(),
        onApply = {},
        onReset = {},
        onCloseButton = {}
    )
}
