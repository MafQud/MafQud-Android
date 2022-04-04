package com.mafqud.android.report.lost

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.DropDownItems
import com.mafqud.android.ui.theme.*


@Composable
@Preview
fun LostScreen(
    isPickedImagesEmpty: MutableState<Boolean> = mutableStateOf(false),
    mPickedImagesUris: MutableState<Int> = mutableStateOf(0),
    pickedImages: MutableList<Uri> = mutableStateListOf("".toUri()),
    openGalleryClicked: () -> Unit = {},
    onCloseClicked: (String) -> Unit = {},
    openImagePreviewer: (List<Uri>, Int) -> Unit = { it, _it -> }
) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp)
            .padding(top = 0.dp),

        ) {
        ColumnUi(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconUi(
                modifier = Modifier.size(77.dp),
                painter = painterResource(id = R.drawable.ic_info),
                tint = MaterialTheme.colorScheme.primary
            )
            TextUi(
                text = stringResource(id = R.string.sorry_for),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            UploadImageButton()
            UploadedImages(
                mPickedImagesUris, pickedImages, openGalleryClicked, onCloseClicked,
                openImagePreviewer, isPickedImagesEmpty
            )

            SpacerUi(modifier = Modifier.height(20.dp))
            LocationForm(onClicked = { i, x ->
            })

        }

    }

}

@Composable
private fun UploadedImages(
    mPickedImagesUris: MutableState<Int>,
    pickedImages: MutableList<Uri>,
    openGalleryClicked: () -> Unit,
    onCloseClicked: (String) -> Unit,
    openImagePreviewer: (List<Uri>, Int) -> Unit,
    isPickedImagesEmpty: MutableState<Boolean>
) {
    val pickedImagesUris = mPickedImagesUris
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    ColumnUi(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardUi(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(4.dp)
        ) {

            DisplayPickedImages(pickedImages, {
                // remove selected image
                pickedImages.remove(it)
                pickedImagesUris.value = pickedImages.size
            }, openGalleryClicked, openImagePreviewer)
        }

        LinearProgressIndicator(
            modifier = Modifier.width(280.dp).clip(RoundedCornerShape(50)),
            progress = animatedProgress,
            color = MaterialTheme.colorScheme.error
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DisplayPickedImages(
    dataList: List<Uri>, onCloseClicked: (Uri) -> Unit,
    openGalleryClicked: () -> Unit, openImagePreviewer: (List<Uri>, Int) -> Unit
) {
    LazyRowUi(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center

    ) {
        items(dataList) { data ->
            DrawImage(
                dataList,
                data,
                onCloseClicked,
                Modifier.animateItemPlacement(),
                openImagePreviewer
            )
        }
    }
}

@Composable
private fun DrawImage(
    dataList: List<Uri>,
    dataItem: Uri,
    onCloseClicked: (Uri) -> Unit,
    animateItemPlacement: Modifier,
    openImagePreviewer: (List<Uri>, Int) -> Unit
) {
    BoxUi(modifier = animateItemPlacement.then(Modifier.clickable {
        // open clicked image in image previewer
        openImagePreviewer(dataList, dataList.indexOf(dataItem))
    }), contentAlignment = Alignment.TopEnd) {
        CardUi(
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(8.dp)
                )
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                }, elevation = 2.dp
        ) {

            ImageUi(
                painter = rememberImagePainter(dataItem),
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }

        BoxUi(
            modifier = Modifier
                .padding(4.dp)
                .background(androidx.compose.material.MaterialTheme.colors.whiteAlways)
                .alpha(0.4f)
                .clickable {
                    onCloseClicked(dataItem)
                }, contentAlignment = Alignment.Center
        ) {
            IconUi(
                imageVector = Icons.Filled.Close,
                tint = androidx.compose.material.MaterialTheme.colors.blackAlways
            )
        }
    }
}


@Composable
fun UploadImageButton() {
    BoxUi(
        modifier = Modifier
            .fillMaxWidth()
            .height(137.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(12.dp))
            .background(
                MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.08f
                )
            )
            .clickable {

            }
            .padding(16.dp),

        ) {
        ColumnUi(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            IconUi(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_info),
                tint = MaterialTheme.colorScheme.primary
            )
            TextUi(
                text = stringResource(id = R.string.upload_image),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            TextUi(
                text = stringResource(id = R.string.upload_image),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LocationForm(onClicked: (Int, Int) -> Unit) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val selectedItem = remember {
            mutableStateOf("")
        }

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.address),
            style = MaterialTheme.typography.titleMedium

        )
        RowUi(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DropDownItems(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                items = listOf("Gov"),
                selectedItemID = selectedItem,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            DropDownItems(
                items = listOf("City"),
                selectedItemID = selectedItem,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
        SpacerUi(modifier = Modifier.height(20.dp))
        ButtonAuth(title = stringResource(id = R.string.next), onClick = {
            onClicked(-1, -1)
        })
    }
}