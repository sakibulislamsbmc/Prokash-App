package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
  primary = ProkahRed,
  onPrimary = Color.White,
  primaryContainer = ProkahRedLight,
  onPrimaryContainer = ProkahRedDark,
  secondary = ProkahOrange,
  onSecondary = Color.White,
  secondaryContainer = ProkahOrangeLight,
  onSecondaryContainer = ProkahOrange,
  tertiary = ProkahGold,
  background = ProkahBg,
  surface = ProkahSurface,
  surfaceVariant = Color(0xFFF9F7F5),
  onBackground = ProkahTextPrimary,
  onSurface = ProkahTextPrimary,
  outline = ProkahCardBorder,
  outlineVariant = ProkahCardBorderLight
)

private val DarkColorScheme = darkColorScheme(
  primary = ProkahOrange,
  onPrimary = Color.White,
  primaryContainer = ProkahRedDark,
  secondary = ProkahRed,
  background = Color(0xFF141312),
  surface = Color(0xFF1E1D1B),
  onBackground = Color(0xFFEEEEEE),
  onSurface = Color(0xFFEEEEEE),
  outline = Color(0xFF33302D)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep brand red consistent
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
