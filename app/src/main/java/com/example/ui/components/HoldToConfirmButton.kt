package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.ProkahOrange
import com.example.ui.theme.ProkahRed
import kotlinx.coroutines.launch

@Composable
fun HoldToConfirmButton(
    language: AppLanguage,
    onConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
    labelBn: String = "নিশ্চিত করতে চেপে ধরে রাখুন",
    labelEn: String = "Tap and hold to confirm"
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val progress = remember { Animatable(0f) }

    fun triggerHaptic(duration: Long = 40) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(duration)
            }
        } catch (_: Exception) {}
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag("hold_to_confirm_button")
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFFFFECEB))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        val job = scope.launch {
                            triggerHaptic(20)
                            progress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 1400, easing = LinearEasing)
                            )
                            if (progress.value >= 0.99f) {
                                triggerHaptic(80)
                                onConfirmed()
                            }
                        }
                        tryAwaitRelease()
                        if (progress.value < 0.99f) {
                            job.cancel()
                            scope.launch {
                                progress.animateTo(0f, animationSpec = tween(200))
                            }
                        }
                    }
                )
            }
    ) {
        // Progress fill
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.value)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(ProkahOrange, ProkahRed)
                    )
                )
        )

        // Center Content & Icon
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular spinner indicator
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (progress.value > 0.5f) Color.White.copy(alpha = 0.3f) else ProkahRed),
                contentAlignment = Alignment.Center
            ) {
                if (progress.value > 0f) {
                    CircularProgressIndicator(
                        progress = { progress.value },
                        modifier = Modifier.size(32.dp),
                        color = Color.White,
                        strokeWidth = 3.dp,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = if (language == AppLanguage.BANGLA) labelBn else labelEn,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (progress.value > 0.45f) Color.White else ProkahRed
            )
        }
    }
}
