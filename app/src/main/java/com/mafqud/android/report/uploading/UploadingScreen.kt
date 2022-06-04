package com.mafqud.android.report.uploading

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.status.loading.CircleLoading
import com.mafqud.android.ui.theme.*


@Composable
@Preview
fun UploadingScreen(
    state: UploadingViewState = UploadingViewState(isUploadingImages = true),
    onTryUploadingImages: () -> Unit = {},
    onTryUploadingCase: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(start = 16.dp, end = 16.dp),

        ) {
        ColumnUi(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            if (state.isSuccess != null) {
                if (state.isSuccess) {
                    var visible by remember { mutableStateOf(state.isSuccess) }
                    val density = LocalDensity.current
                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInVertically {
                            // Slide in from 40 dp from the top.
                            with(density) { -40.dp.roundToPx() }
                        } + expandVertically(
                            // Expand from the top.
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            // Fade in with the initial alpha of 0.3f.
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {
                        UploadingSuccess(onConfirm)
                    }
                }
            } else {
                ImageAndCaseStates(state, onTryUploadingImages, onTryUploadingCase)
            }
        }
    }
}

@Composable
fun ImageAndCaseStates(
    state: UploadingViewState,
    onTryUploadingImages: () -> Unit,
    onTryUploadingCase: () -> Unit
) {
    GifImage(
        modifier = Modifier
            .size(160.dp, 190.dp),
        imageID = R.drawable.animation
    )
    /**
     * images states
     */
    if (state.isUploadingImages) {
        UploadingImages()
    }
    if (state.isSuccessUploadingImages) {
        SuccessImagesUploaded()
    }
    if (state.isFailedUploadingImages) {
        FailedToUploadImages(onTryUploadingImages)
    }

    /**
     * case states
     */
    if (state.isUploadingCase) {
        UploadingCase()
    }
    /*if (state.isSuccessUploadingCase) {
        SuccessCase()
    }*/

    if (state.isFailedUploadingCase) {
        FailedToUploadCase(onTryUploadingCase)
    }
}

@Composable
fun SuccessCase() {
    TextUi(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.success_uploading_case),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
fun SuccessImagesUploaded() {
    RowUi(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(Modifier.size(20.dp)) {
            IconUi(
                painter = painterResource(id = R.drawable.ic_state_success),
                modifier = Modifier.size(24.dp)
            )
        }
        TextUi(
            text = stringResource(id = R.string.success_uploading_images),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
fun FailedToUploadCase(onTryUploadingCase: () -> Unit) {
    RowUi(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TextUi(
            text = stringResource(id = R.string.failed_loading_case),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
        // next
        TextUi(
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onTryUploadingCase()
                }
                .padding(8.dp),
            text = stringResource(id = R.string.try_again),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FailedToUploadImages(onTryUploadingImages: () -> Unit) {
    RowUi(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TextUi(
            text = stringResource(id = R.string.failed_loading_images),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
        // next
        TextUi(
            modifier = Modifier
                //.width(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onTryUploadingImages()
                }
                .padding(8.dp),
            text = stringResource(id = R.string.try_again),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun UploadingCase() {
    RowUi(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(Modifier.size(20.dp)) {
            CircleLoading(loadingState = true)
        }
        TextUi(
            text = stringResource(id = R.string.loading_case),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun UploadingImages() {
    RowUi(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxUi(Modifier.size(20.dp)) {
            CircleLoading(loadingState = true)
        }
        TextUi(
            text = stringResource(id = R.string.loading),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )

    }
}

@Composable
fun UploadingSuccess(
    onConfirm: () -> Unit = {}
) {
    ColumnUi(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        BoxUi(modifier = Modifier) {
            ColumnUi {
                SpacerUi(modifier = Modifier.height(70.dp))
                BodyText()
            }
            IconUi(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(120.dp, 100.dp),
                painter = painterResource(id = R.drawable.ic_warning),
                tint = MaterialTheme.colorScheme.primary
            )
        }


        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.search_lost_2),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )

        // next
        TextUi(
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onConfirm()
                }
                .padding(8.dp),
            text = stringResource(id = R.string.confirm),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun BodyText() {
    BoxUi(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20.dp),
            )
            .background(
                MaterialTheme.colorScheme.onPrimary
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        TextUi(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.search_lost),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

    }

}
