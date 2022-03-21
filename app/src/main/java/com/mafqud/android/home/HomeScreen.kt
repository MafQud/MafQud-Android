package com.mafqud.android.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.compose.CaseItem
import com.mafqud.android.ui.compose.DropDownItems
import com.mafqud.android.ui.theme.*

@Composable
fun HomeScreen() {
    ColumnUi(
        Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HeadSearchUi()
        BodyUi(Modifier.weight(1f))
    }
}

@Composable
fun HeadSearchUi() {
    BoxUi(
        Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        SearchUi()
    }
}


@Composable
private fun SearchUi() {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TapsUi()
        DropDownUi()
        SearchNameUi()
    }
}

@Composable
private fun SearchNameUi() {
    val searchName = remember {
        mutableStateOf("")
    }
    TextFieldUi(
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions { /*onTypeListener(searchWords.value)*/ },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            TextUi(
                text = stringResource(id = R.string.search_title),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall
            )
        },
        shape = RoundedCornerShape(50),
        value = searchName.value,
        onValueChange = {
            searchName.value = it
            //onTypeListener(it)
        },
        singleLine = true,
        leadingIcon = {
            IconUi(
                imageVector = Icons.Outlined.Search,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.onSecondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun DropDownUi() {
    RowUi(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        val selectedItem = remember {
            mutableStateOf("")
        }
        DropDownItems(
            items = listOf("Age"),
            selectedItemID = selectedItem,
            modifier = Modifier
                .weight(1f)
                .height(30.dp),
        )
        DropDownItems(
            items = listOf("Date"),
            selectedItemID = selectedItem,
            modifier = Modifier
                .weight(1f)
                .height(30.dp),
        )
        DropDownItems(
            items = listOf("City"),
            selectedItemID = selectedItem,
            modifier = Modifier
                .weight(1f)
                .height(30.dp),
        )

    }
}

@Composable
private fun TapsUi() {
    RowUi {
        val selectedItem = remember {
            mutableStateOf(0)
        }

        val activeTextColor = MaterialTheme.colorScheme.onSecondary
        val activeBackgroundColor = MaterialTheme.colorScheme.primary


        val disableTextColor = MaterialTheme.colorScheme.primary
        val disableBackgroundColor = MaterialTheme.colorScheme.onSecondary

        BoxUi(
            Modifier
                .height(30.dp)
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = activeBackgroundColor,
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                )
                .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                .background(if (selectedItem.value == 0) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    selectedItem.value = 0
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = "All",
                color = if (selectedItem.value == 0) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }

        //
        BoxUi(
            Modifier
                .height(30.dp)
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = activeBackgroundColor,
                    shape = RectangleShape
                )
                .background(if (selectedItem.value == 1) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    selectedItem.value = 1

                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = "Lost",
                color = if (selectedItem.value == 1) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }

        //
        BoxUi(
            Modifier
                .height(30.dp)
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = activeBackgroundColor,
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                )
                .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                .background(if (selectedItem.value == 2) activeBackgroundColor else disableBackgroundColor)
                .clickable {
                    selectedItem.value = 2
                },
            contentAlignment = Alignment.Center
        ) {
            TextUi(
                text = "Found",
                color = if (selectedItem.value == 2) activeTextColor else disableTextColor,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}


@Composable
private fun BodyUi(modifier: Modifier) {
    ColumnUi(
        modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        CaseItem()
        CaseItem()
        CaseItem()
        CaseItem()
        CaseItem()
    }
}