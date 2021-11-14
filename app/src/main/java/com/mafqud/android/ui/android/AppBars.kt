package com.mafqud.android.ui.android

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mafqud.android.R

import com.mafqud.android.ui.material.*

@Composable
private fun backgroundColor() = MaterialTheme.colors.white100Always
private val appBarHeight = 60.dp
private val iconSize = 40.dp

@Composable
private fun gradientColors() = listOf(
    MaterialTheme.colors.redBoldAlways,
    MaterialTheme.colors.purpleAlways
)

@Composable
private fun Modifier.underLineAppBar(): Modifier {
    val color = MaterialTheme.colors.gray200All
    return this.then(drawWithContent {
        drawContent()
        clipRect { // Not needed if you do not care about painting half stroke outside
            val strokeWidth = Stroke.DefaultMiter
            val y = size.height // - strokeWidth
            // if the whole line should be inside component
            drawLine(
                brush = SolidColor(color),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Square,
                start = Offset.Zero.copy(y = y),
                end = Offset(x = size.width, y = y)
            )
        }
    }.height(appBarHeight))
}

@Composable
fun AppBarWithTitle(title: String) {
    BoxUi(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor())
            .underLineAppBar()
            .padding(8.dp)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        TextUi(title)
    }

}

@Composable
fun AppBarWithTitleAndBack(
    title: String,
    hasBottomBorder: Boolean = true,
    backgroundColor: Color = backgroundColor(),
    onBackClicked: () -> Unit
) {
    BoxUi(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .also {
                if (hasBottomBorder)
                    it.underLineAppBar()
            }
            .padding(8.dp)
        ,
        contentAlignment = Alignment.CenterStart
    ) {
        BoxUi(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.gray200All)
                .clickable {
                    onBackClicked()
                }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            //IconUi(painter = painterResource(id = R.drawable.ic_back))
        }
        BoxUi(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            TextUi(title)
        }

    }
}

@Composable
fun AppBarWithTitleAndSearchAndFilter(
    title: String = stringResource(id = R.string.app_name),
    onSearchClicked: () -> Unit, onFilterClicked: () -> Unit
) {
    RowUi(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor())
            .underLineAppBar()
            .padding(8.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.gray200All)
                .clickable {
                    onSearchClicked()
                }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
           // IconUi(painter = painterResource(id = R.drawable.ic_search))
        }
        TextUi(title)


        BoxUi(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.gray200All)
                .clickable {
                    onFilterClicked()

                }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            //IconUi(painter = painterResource(id = R.drawable.ic_filter))
        }

    }
}

@Composable
fun AppBarWithTitleAndBackAndFilter(
    title: String = stringResource(id = R.string.app_name),
    onBackClicked: () -> Unit, onFilterClicked: () -> Unit
) {
    RowUi(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor())
            .underLineAppBar()
            .padding(8.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.gray200All)
                .clickable {
                    onBackClicked()
                }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            //IconUi(painter = painterResource(id = R.drawable.ic_back))
        }
        TextUi(title)


        BoxUi(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.gray200All)
                .clickable {
                    onFilterClicked()

                }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            IconUi(imageVector = Icons.Outlined.FilterList)
        }

    }
}

@Composable
fun AppBarWithBackAndButton(
    buttonTitle: String = "",
    onBackClicked: () -> Unit, onButtonClicked: () -> Unit
) {
    RowUi(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = android.R.color.transparent))
            .padding(8.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.gray200All)
                .clickable {
                    onBackClicked()
                }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            //IconUi(painter = painterResource(id = R.drawable.ic_back))
        }

        GeneralButton(
            modifier = Modifier.height(iconSize),
            cornerSize = 4.dp,
            title = buttonTitle,
            onClicked = onButtonClicked,
            buttonColor = MaterialTheme.colors.purpleAlways
        )

    }
}

@Composable
fun AppBarWithBackAndTitleAndButton(
    title: String = "",
    buttonTitle: String = "",
    onBackClicked: () -> Unit, onButtonClicked: () -> Unit
) {
    RowUi(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor())
            .underLineAppBar()
            .padding(6.dp)
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.gray200All)
                .clickable {
                    onBackClicked()
                }
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            //IconUi(painter = painterResource(id = R.drawable.ic_back))
        }
        TextUi(title)


        GeneralButton(
            cornerSize = 4.dp,
            modifier = Modifier.height(iconSize),
            title = buttonTitle,
            onClicked = onButtonClicked,
            buttonColor = MaterialTheme.colors.purpleAlways
        )

    }
}