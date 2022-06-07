package com.mafqud.android.ui.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.mafqud.android.R
import com.mafqud.android.util.other.LogMe


@Composable
fun LoadImageAsync(
    modifier: Modifier,
    imageUrl: String?,
    @DrawableRes placeholder: Int = R.drawable.placeholder_image,
    @DrawableRes onError: Int = R.drawable.error_image,
    cacheEnabled: Boolean = true,
    contentScale : ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl.toCorrectImageUrl())
            .crossfade(true)
            .also {
                if (cacheEnabled) {
                    it.diskCachePolicy(CachePolicy.ENABLED)
                }
            }
            .build(),
        placeholder = painterResource(placeholder),
        contentDescription = "Image",
        contentScale = contentScale,
        error = painterResource(onError),
        modifier = modifier
    )
}

fun String?.toCorrectImageUrl(): String {
    //LogMe.i("old_url", imageUrl)
    val newBase = this?.replaceAfter(delimiter = "s3", replacement = ".eu-south-1.amazonaws.com")
    val file = "/files"
    val filesPath = this?.substringAfter(file)
    val newUrl = newBase + file + filesPath
    LogMe.i("new_url", newUrl)
    return newUrl
}