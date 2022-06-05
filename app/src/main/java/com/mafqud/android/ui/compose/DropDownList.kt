package com.mafqud.android.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowDropDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mafqud.android.report.lost.Gender
import com.mafqud.android.ui.theme.*
import com.mafqud.android.util.other.LogMe

@Composable
fun DropDownItems(
    title: String = "",
    items: List<String>,
    selectedItemTitle: MutableState<String> = mutableStateOf(""),
    modifier: Modifier,
    iconColor: Color = MaterialTheme.colorScheme.onSecondary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    onSelectItem: (String) -> Unit = {},
) {
    val expanded = remember { mutableStateOf(false) }
    val govTitle = title
    val selectedText = remember { mutableStateOf(govTitle) }

    if (selectedItemTitle.value == "-1") {
        LogMe.i("selectedItemID" , "tr")
        selectedText.value = govTitle
    } else {
        selectedText.value = selectedItemTitle.value
    }

    BoxUi(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(horizontal = 4.dp)
            .clickable {
                expanded.value = !expanded.value
            },
        contentAlignment = Alignment.Center
    ) {
        // Back arrow here
        RowUi(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) { // Anchor view
            TextUi(
                modifier = Modifier.padding(start = 4.dp),
                text = selectedText.value, style = MaterialTheme.typography.titleSmall,
                color = textColor
            ) // City name label
            IconUi(
                imageVector = Icons.TwoTone.ArrowDropDown,
                tint = iconColor
            )
        }

        DropDownMenuUi(expanded = expanded.value,
            modifier = Modifier.fillMaxWidth(), content = {
                DropdownMenuItem(onClick = {
                    selectedText.value = govTitle
                    //selectedItemID.value = index.toString()
                    expanded.value = false
                    onSelectItem("-1")
                }) {
                    TextUi(
                        modifier = Modifier.padding(4.dp),
                        text = govTitle,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                items.forEachIndexed { index, label ->
                    DropdownMenuItem(onClick = {
                        selectedText.value = label
                        //selectedItemID.value = index.toString()
                        expanded.value = false
                        onSelectItem(index.toString())
                    }) {
                        TextUi(
                            modifier = Modifier.padding(4.dp),
                            text = label,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }
            }, onDismissRequest = { expanded.value = false })
    }
}


@Composable
fun DropDownGender(
    items: List<String>,
    modifier: Modifier,
    iconColor: Color = MaterialTheme.colorScheme.onSecondary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    onSelectItem: (Gender) -> Unit = {},
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedText = remember { mutableStateOf(items.first()) }

    BoxUi(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(horizontal = 4.dp)
            .clickable {
                expanded.value = !expanded.value
            },
        contentAlignment = Alignment.Center
    ) {
        // Back arrow here
        RowUi(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) { // Anchor view
            TextUi(
                modifier = Modifier.padding(start = 4.dp),
                text = selectedText.value, style = MaterialTheme.typography.titleSmall,
                color = textColor
            ) // City name label
            IconUi(
                imageVector = Icons.TwoTone.ArrowDropDown,
                tint = iconColor
            )
        }

        DropDownMenuUi(expanded = expanded.value,
            modifier = Modifier.fillMaxWidth(), content = {
                items.forEachIndexed { index, label ->
                    DropdownMenuItem(onClick = {
                        selectedText.value = label
                        expanded.value = false
                        val gender = when (index) {
                            0 -> Gender.NONE
                            1 -> Gender.MALE
                            2 -> Gender.FEMALE
                            else -> Gender.NONE
                        }
                        onSelectItem(gender)
                    }) {
                        TextUi(
                            modifier = Modifier.padding(4.dp),
                            text = label,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }
            }, onDismissRequest = { expanded.value = false })
    }
}