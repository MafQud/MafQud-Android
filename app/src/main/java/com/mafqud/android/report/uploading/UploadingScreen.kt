package com.mafqud.android.report.uploading

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.theme.*


@Composable
@Preview
fun UploadingScreen() {
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
            GifImage(
                modifier = Modifier
                    .size(160.dp, 190.dp),
                imageID = R.drawable.animation
            )

            TextUi(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.loading),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            UploadingSuccess()

        }

    }
}

@Composable
fun UploadingSuccess() {
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
