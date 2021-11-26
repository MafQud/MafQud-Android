package com.mafqud.android.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val redBold = Color(0xFFFF6B6B)
val white = Color(0xFFffffff)
val redLight = Color(0xFFFFEAEA)
val blueLight = Color(0xFFA3B6C4)
val black = Color(0xFF000000)
val gray = Color(0xFF999999)
val gray200 = Color(0xFFF0F0F0)
val white200 = Color(0xFFF2F2F2)
val gray400 = Color(0xFF999999)
val purple = Color(0xFF7E44DF)
val purpleLight = Color(0xFFF0E9FA)
val blue = Color(0xFF3B5999)
val white100 = Color(0xFFFAFAFA)
val greenLight = Color(0xFFD9FFE3)
val greenBold = Color(0xFF28B446)
val yellowBold = Color(0xFFFFB800)
val yellow500 = Color(0xFFEFA802)
val yellowLight = Color(0xFFFFF8E5)
val gray800 = Color(0xFF555555)

val Colors.redBoldToGray: Color
    @Composable get() = if (isLight) redBold else gray

val Colors.purpleAlways: Color
    @Composable get() = if (isLight) purple else purple


val Colors.gray200ToBlueLight: Color
    @Composable get() = if (isLight) gray400 else blueLight

val Colors.gray200ToGray600: Color
    @Composable get() = if (isLight) gray400 else gray

val Colors.gray200All: Color
    @Composable get() = if (isLight) gray200 else gray200

val Colors.gray800All: Color
    @Composable get() = if (isLight) gray800 else gray800
val Colors.gray400All: Color
    @Composable get() = if (isLight) gray400 else gray400

val Colors.redBoldToBlueLight: Color
    @Composable get() = if (isLight) redBold else blueLight

val Colors.whiteToBlack: Color
    @Composable get() = if (isLight) white else black

val Colors.whiteToGray: Color
    @Composable get() = if (isLight) white else gray

val Colors.blueBoldToGray: Color
    @Composable get() = if (isLight) blue else gray

val Colors.white200Always: Color
    @Composable get() = if (isLight) white200 else white200

val Colors.white100Always: Color
    @Composable get() = if (isLight) white100 else white100

val Colors.redBoldToLight: Color
    @Composable get() = if (isLight) redBold else redLight

val Colors.redBoldToWhite: Color
    @Composable get() = if (isLight) redBold else white

val Colors.blackToWhite: Color
    @Composable get() = if (isLight) black else white

val Colors.blackToRedLight: Color
    @Composable get() = if (isLight) black else redLight

val Colors.redLightAll: Color
    @Composable get() = if (isLight) redLight else redLight

val Colors.greenLightAll: Color
    @Composable get() = if (isLight) greenLight else greenLight

val Colors.greenBoldAll: Color
    @Composable get() = if (isLight) greenBold else greenBold

val Colors.purpleLightAll: Color
    @Composable get() = if (isLight) purpleLight else purpleLight

val Colors.yellowBoldAll: Color
    @Composable get() = if (isLight) yellowBold else yellowBold

val Colors.yellow500All: Color
    @Composable get() = if (isLight) yellow500 else yellow500

val Colors.yellowLightAll: Color
    @Composable get() = if (isLight) yellowLight else yellowLight


val Colors.blueBoldToLight: Color
    @Composable get() = if (isLight) blue else blueLight

val Colors.blueAlways: Color
    @Composable get() = if (isLight) blue else blue

val Colors.blackAlways: Color
    @Composable get() = black

val Colors.whiteAlways: Color
    @Composable get() = white

val Colors.redBoldAlways: Color
    @Composable get() = redBold

val Colors.whiteToRedLight: Color
    @Composable get() = if (isLight) white else redLight