package com.mafqud.android.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mafqud.android.BuildConfig
import com.mafqud.android.R
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.GifImage
import com.mafqud.android.ui.theme.TextUi


@Preview("Splash")
@Composable
fun SplashScreen(onAnimationFinished: () -> Unit = {}) {
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {
        val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.mafqud))
        val mProgress = animateLottieCompositionAsState(composition.value)

        // to be as indicator for the users to know, whose version he is working on.
        BoxUi(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.material3.MaterialTheme.colorScheme.background
                )
        ) {

            /* LottieAnimation(
                 modifier = Modifier
                     .size(300.dp, 220.dp)
                     .align(Alignment.Center),
                 composition = composition.value,
                 progress = mProgress.value,
             )*/
            GifImage(
                modifier = Modifier
                    .size(300.dp, 220.dp)
                    .align(Alignment.Center),
                imageID = R.drawable.animation_logo
            )

            ColumnUi(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextUi(
                    text = BuildConfig.VERSION_NAME.toLowerCase(),
                    fontSize = 14.sp
                )
                if (!BuildConfig.IS_STABLE) {
                    TextUi(text = stringResource(id = R.string.note_release), fontSize = 14.sp)
                }
            }

        }
        /*Handler(Looper.getMainLooper()).postDelayed({
            onAnimationFinished()
        }, 4000)*/

        /*if (mProgress.value == 1f) {
            onAnimationFinished()
        } else if (composition.isFailure) {
            onAnimationFinished()
        }*/
    }
}