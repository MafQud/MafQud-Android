package com.mafqud.android.ui.theme

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Typeface
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.PopupProperties
import com.google.accompanist.pager.*
import com.mafqud.android.R
import kotlin.math.absoluteValue


@Composable
fun TextUi(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = 5,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        AnnotatedString(text),
        modifier,
        color,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        emptyMap(),
        onTextLayout,
        style
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableTextUi(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    softWrap: Boolean = true,
    minimizedMaxLines: Int = 2,
    style: TextStyle = LocalTextStyle.current,
    expandableTextColor: Color = MaterialTheme.colors.gray400All,
    expandableTextContent: String = stringResource(id = R.string.see_more),
    expandableTextStyle: TextStyle = MaterialTheme.typography.h6,
) {
    val cutText = remember(text) { mutableStateOf<String?>(null) }
    val expanded = remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    val seeMoreSizeState = remember { mutableStateOf<IntSize?>(null) }
    val seeMoreOffsetState = remember { mutableStateOf<Offset?>(null) }

    // getting raw values for smart cast
    val textLayoutResult = textLayoutResultState.value
    val seeMoreSize = seeMoreSizeState.value
    val seeMoreOffset = seeMoreOffsetState.value

    LaunchedEffect(text, expanded, textLayoutResult, seeMoreSize) {

        val lastLineIndex = minimizedMaxLines - 1
        if (!expanded.value && textLayoutResult != null && seeMoreSize != null
            && lastLineIndex + 1 == textLayoutResult.lineCount
            && textLayoutResult.isLineEllipsized(lastLineIndex)
        ) {
            var lastCharIndex = textLayoutResult.getLineEnd(lastLineIndex, visibleEnd = true) + 1
            var charRect: Rect
            do {
                lastCharIndex -= 1
                charRect = textLayoutResult.getCursorRect(lastCharIndex)
            } while (
                charRect.left > textLayoutResult.size.width - seeMoreSize.width
            )
            seeMoreOffsetState.value = Offset(charRect.left, charRect.bottom - seeMoreSize.height)
            cutText.value = text.substring(startIndex = 0, endIndex = lastCharIndex)
        }
    }

    Box(modifier = Modifier.clickable {
        if (!expanded.value) {
            expanded.value = true
            cutText.value = null
        } else {
            expanded.value = false
            cutText.value = text
        }
    }) {
        Text(
            text = cutText.value ?: text,
            maxLines = if (expanded.value) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResultState.value = it },
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            softWrap = softWrap,
            style = style
        )
        if (!expanded.value) {
            val density = LocalDensity.current
            Text(
                color = expandableTextColor,
                style = expandableTextStyle,
                text = expandableTextContent,
                onTextLayout = { seeMoreSizeState.value = it.size },
                modifier = Modifier
                    .then(
                        if (seeMoreOffset != null)
                            Modifier.offset(
                                x = with(density) { seeMoreOffset.x.toDp() },
                                y = with(density) { seeMoreOffset.y.toDp() },
                            )
                        else
                            Modifier
                    )
                    .clickable {
                        expanded.value = true
                        cutText.value = null
                    }
                    .alpha(if (seeMoreOffset != null) 1f else 0f)
            )


        }
    }
}

@Composable
fun TextFieldUi(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(8.dp),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = MaterialTheme.colors.gray200All,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
    )
) {
    TextField(
        value,
        onValueChange,
        modifier,
        enabled,
        readOnly,
        textStyle,
        label,
        placeholder,
        leadingIcon,
        trailingIcon,
        isError,
        visualTransformation,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        interactionSource,
        shape,
        colors
    )
}

@Composable
fun BoxUi(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier, contentAlignment, propagateMinConstraints, content
    )
}

@Composable
fun ImageUi(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = "",
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    Image(painter, contentDescription, modifier, alignment, contentScale, alpha, colorFilter)
}

@Composable
fun ButtonUi(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick,
        modifier,
        enabled,
        interactionSource,
        elevation,
        shape,
        border,
        colors,
        contentPadding,
        content
    )
}


@ExperimentalPagerApi
@Composable
fun SliderHorizontalUi(
    count: Int,
    content: @Composable (Int, PagerState) -> Unit
) {

    val pagerState = rememberPagerState( )

    ColumnUi(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(state = pagerState, count = count) { page ->
            CardUi(
                Modifier
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        // We animate the scaleX + scaleY, between 85% and 100%
                        androidx.compose.ui.util
                            .lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            .also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }
                        // We animate the alpha, between 50% and 100%
                        alpha = androidx.compose.ui.util.lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .fillMaxWidth(), backgroundColor = Color.Transparent, elevation = 0.dp
            ) {
                // Card content
                BoxUi(
                    modifier = Modifier.requiredHeightIn(300.dp, 380.dp),
                    contentAlignment = Alignment.Center
                ) {
                    content(page, pagerState)
                }
            }
        }
        /**
         *  HorizontalPagerIndicator
         */
        /**
         *  HorizontalPagerIndicator
         */
        HorizontalPagerIndicator(
            indicatorWidth = 12.dp,
            indicatorHeight = 12.dp,
            pagerState = pagerState,
            modifier = Modifier
                .padding(16.dp)
                .requiredSizeIn(50.dp, 30.dp),
            activeColor = MaterialTheme.colors.whiteAlways,
            inactiveColor = MaterialTheme.colors.gray200ToBlueLight
        )


        //footer?.invoke(pagerState)
    }
    /* VerticalPager(state = pagerState) { page ->
         content(page, pagerState)
     }*/

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /*   ActionsRow(
               pagerState = pagerState,
               modifier = Modifier.align(Alignment.CenterHorizontally)
           )*/
    }
}

@ExperimentalPagerApi
@Composable
fun SliderVerticalUi(
    count: Int,
    content: @Composable (Int, PagerState) -> Unit
) {

    val pagerState = rememberPagerState()

    ColumnUi(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        VerticalPager(state = pagerState, count = count) { page ->
            CardUi(
                Modifier
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        // We animate the scaleX + scaleY, between 85% and 100%
                        androidx.compose.ui.util
                            .lerp(
                                start = 0.90f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            .also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }
                        // We animate the alpha, between 50% and 100%
                        alpha = androidx.compose.ui.util.lerp(
                            start = 0.2f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .fillMaxWidth(), backgroundColor = Color.Transparent, elevation = 0.dp
            ) {
                // Card content
                BoxUi(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    content(page, pagerState)
                }
            }
        }

    }
}

@Composable
fun CardUi(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(0),
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    border: BorderStroke? = null,
    elevation: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    Card(modifier, shape, backgroundColor, contentColor, border, elevation, content)

}

@Composable
fun ColumnUi(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier, verticalArrangement, horizontalAlignment, content)
}

@Composable
fun RowUi(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit
) {
    Row(modifier, horizontalArrangement, verticalAlignment, content)
}


@Composable
fun LazyRowUi(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier,
        state,
        contentPadding,
        reverseLayout,
        horizontalArrangement,
        verticalAlignment,
        flingBehavior,
        content
    )
}

@Composable
fun LazyColumnUi(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier,
        state,
        contentPadding,
        reverseLayout,
        verticalArrangement,
        horizontalAlignment,
        flingBehavior,
        content
    )
}

@Composable
fun IconUi(
    painter: Painter,
    contentDescription: String? = "",
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    Icon(painter, contentDescription, modifier, tint)
}

@Composable
fun HorizontalLine() {
    SpacerUi(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(2.dp)
            .alpha(.5f)
            .background(MaterialTheme.colors.gray200All)
    )
}

@Composable
fun IconUi(
    imageVector: ImageVector,
    contentDescription: String? = "",
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    Icon(imageVector, contentDescription, modifier, tint)
}

@Composable
fun SpacerUi(modifier: Modifier) {
    Spacer(modifier)
}


@Composable
fun SpacerSmallLine() {
    ColumnUi(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        SpacerUi(
            modifier = Modifier
                .width(50.dp)
                .height(2.dp)
                .background(MaterialTheme.colors.redBoldToLight)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(2.dp))
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun GridUi(
    cells: GridCells,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        cells,
        modifier,
        state,
        contentPadding,
        verticalArrangement,
        horizontalArrangement,
        content
    )
}

@ExperimentalFoundationApi
@Composable
fun LazyGridUi(
    cells: GridCells,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        cells,
        modifier,
        state,
        contentPadding,
        verticalArrangement,
        horizontalArrangement,
        content
    )
}


@Composable
fun createAndroidViewForXMLLayout(@LayoutRes resId: Int, modifier: Modifier) {
    val context = LocalContext.current
    val your_xml_Layout = remember(resId, context) {
        LayoutInflater.from(context).inflate(resId, null)
    }
    AndroidView(modifier = modifier, factory = { your_xml_Layout })
}

@Composable
fun DropDownMenuUi(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit
) {
    DropdownMenu(expanded, onDismissRequest, modifier, offset, properties, content)
}

@Composable
fun GradientTextUi(name: String, colors: List<Color>, modifier: Modifier = Modifier) {

    BoxUi(modifier = Modifier.size(50.dp)) {
        val paint = Paint().asFrameworkPaint()

        val gradientShader: Shader = LinearGradientShader(
            from = Offset(0f, 0f),
            to = Offset(0f, 20f),
            colors
        )

        Canvas(modifier) {
            paint.apply {
                isAntiAlias = true
                textSize = 20f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                style = android.graphics.Paint.Style.FILL
                color = android.graphics.Color.parseColor("#cdcdcd")
                xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
            }
            drawIntoCanvas { canvas ->
                canvas.save()
                canvas.nativeCanvas.translate(2f, 5f)
                canvas.nativeCanvas.drawText(name, 0f, 20f, paint)
                canvas.restore()
                paint.shader = gradientShader
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                paint.maskFilter = null
                canvas.nativeCanvas.drawText(name, 0f, 20f, paint)
                canvas.nativeCanvas.translate(2f, 5f)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                paint.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
                canvas.nativeCanvas.drawText(name, 0f, 10f, paint)
            }
            paint.reset()
        }
    }
}

/*@Composable
fun gradiantTextUi(text: String) {
    val context = LocalContext.current
    val your_xml_Layout = remember(R.layout.grad_text, context) {
        val view = LayoutInflater.from(context).inflate(R.layout.grad_text, null)
        val textView = view.findViewById<TextView>(R.id.text)
        textView.text = text
        *//* val shader = LinearGradient(0f, 0f, 0f, textView.textSize,
             Color.RED, Color.BLUE,
             Shader.TileMode.CLAMP)
         textView.paint.shader = shader*//*
        return@remember view
    }
    AndroidView(modifier = Modifier, factory = { your_xml_Layout })
}*/

/*
@Composable
fun SwipeToRefresh(
    content: @Composable () -> Unit,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
    ) {
        content()
    }
}*/
