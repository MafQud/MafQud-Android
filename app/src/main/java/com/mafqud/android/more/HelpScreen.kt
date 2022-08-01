package com.mafqud.android.more

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mafqud.android.R
import com.mafqud.android.ui.theme.BoxUi
import com.mafqud.android.ui.theme.ColumnUi
import com.mafqud.android.ui.theme.TextUi

@Composable
@Preview
fun HelpScreen() {
    val url = remember { mutableStateOf("https://gist.github.com/ahmed-shehataa/aae20217432b3679e1fce357cbc46f5d") }
    val visibility = remember { mutableStateOf(true) }
    BoxUi(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp),
        contentAlignment = Alignment.TopStart

    ) {
        ColumnUi(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextUi(
                text = stringResource(id = R.string.privcy),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start
            )

            AndroidView(factory = { context ->
                WebView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(
                            view: WebView, url: String,
                            favicon: Bitmap?
                        ) {
                            visibility.value = true
                        }

                        override fun onPageFinished(
                            view: WebView, url: String
                        ) {
                            visibility.value = false
                        }
                    }

                    loadUrl("https://www.google.com")
                }
            }, update = {
                it.loadUrl(url.value)
            })

        }

        if (visibility.value) {
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.Center)
                    .matchParentSize(),
                visible = visibility.value,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        //.background(MaterialTheme.colorScheme.surfaceVariant)
                        .wrapContentSize()
                )
            }
        }
    }

}