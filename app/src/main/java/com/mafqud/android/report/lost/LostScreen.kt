package com.mafqud.android.report.lost

import android.net.Uri
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.mafqud.android.R
import com.mafqud.android.ui.compose.ButtonAuth
import com.mafqud.android.ui.compose.DropDownItems
import com.mafqud.android.ui.compose.dashedBorder
import com.mafqud.android.ui.theme.*


@Composable
@Preview
fun LostScreen(
    //mPickedImagesUris: MutableState<Int>,
    maxPhotos: Int = 10,
    pickedImages: List<Uri> = mutableStateListOf("".toUri()),
    openGalleryClicked: () -> Unit = {},
    onCloseClicked: (Uri) -> Unit = {},
    openImagePreviewer: (List<Uri>, Int) -> Unit = { it, _it -> },
    onNextClicked: () -> Unit = {},
) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(start = 16.dp, end = 16.dp),

        ) {
        ColumnUi(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var isFormActivated by remember {
                mutableStateOf(false)
            }
            var progress by remember { mutableStateOf(0f) }
            val currentPickedImages = pickedImages.size
            progress = currentPickedImages.toFloat() / maxPhotos.toFloat()
            isFormActivated = !(currentPickedImages < 3 || currentPickedImages > 10)

            SpacerUi(modifier = Modifier.height(16.dp))
            Header()
            SpacerUi(modifier = Modifier.height(20.dp))
            UploadImageButton(openGalleryClicked)
            UploadingPhotosInfo()
            UploadedImages(
                pickedImages,
                progress,
                isFormActivated,
                openImagePreviewer,
                onCloseClicked
            )
            SpacerUi(modifier = Modifier.height(20.dp))
            LocationForm(isFormActivated) { i, x ->
                onNextClicked()
            }
            SpacerUi(modifier = Modifier.height(16.dp))

        }

    }

}

@Composable
fun UploadingPhotosInfo() {
    RowUi(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DotText(stringResource(id = R.string.upload_inst1))
        DotText(stringResource(id = R.string.upload_inst2))

    }
}

@Composable
fun DotText(text: String) {
    RowUi(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        CircleDot()
        TextUi(
            text = text,
            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun CircleDot() {
    BoxUi(
        modifier = Modifier
            .size(10.dp)
            .clip(
                RoundedCornerShape(50)
            )
            .background(MaterialTheme.colorScheme.error)

    ) {

    }
}

@Composable
fun Header() {
    IconUi(
        modifier = Modifier.size(77.dp),
        painter = painterResource(id = R.drawable.ic_info),
        tint = MaterialTheme.colorScheme.primary
    )
    TextUi(
        text = stringResource(id = R.string.sorry_for),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun UploadedImages(
    pickedImages: List<Uri>,
    progress: Float,
    isFormActivated: Boolean,
    openImagePreviewer: (List<Uri>, Int) -> Unit,
    onCloseClicked: (Uri) -> Unit,
) {
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    val animatedColor = animateColorAsState(
        if (isFormActivated) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    )

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
                //pickedImages.remove(it)
                onCloseClicked(it)
            }, openImagePreviewer)

        }

        SpacerUi(modifier = Modifier.height(8.dp))

        if (progress != 0f) {
            RowUi(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50)),
                    progress = animatedProgress,
                    color = animatedColor.value
                )

                val photosCount = (progress * 10).toInt()
                //counter message
                TextUi(
                    text = "$photosCount / 10",
                    textAlign = TextAlign.End,
                    color = animatedColor.value,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DisplayPickedImages(
    dataList: List<Uri>, onCloseClicked: (Uri) -> Unit,
    openImagePreviewer: (List<Uri>, Int) -> Unit
) {
    LazyRowUi(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically

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
                .padding(2.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.onSecondary)
                .alpha(0.4f)
                .clickable {
                    onCloseClicked(dataItem)
                }, contentAlignment = Alignment.Center
        ) {
            IconUi(
                imageVector = Icons.Filled.Close,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


@Composable
fun UploadImageButton(openGalleryClicked: () -> Unit) {
    BoxUi(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .dashedBorder(
                width = 2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(20.dp),
                on = 4.dp, off = 4.dp
            )
            .background(
                MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.08f
                )
            )
            .clickable {
                openGalleryClicked()
            }
            .padding(16.dp),

        ) {
        ColumnUi(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            IconUi(
                modifier = Modifier.size(30.dp, 27.dp),
                imageVector = Icons.Filled.Upload,
                tint = MaterialTheme.colorScheme.primary
            )
            TextUi(
                text = stringResource(id = R.string.upload_image),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun LocationForm(isFormActivated: Boolean, onClicked: (Int, Int) -> Unit) {
    ColumnUi(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val selectedItem = remember {
            mutableStateOf("")
        }

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.address_lost),
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
        ButtonAuth(
            enabled = isFormActivated,
            title = stringResource(id = R.string.next),
            onClick = {
                onClicked(-1, -1)
            })
    }
}