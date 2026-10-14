package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.ProkahCardBorder
import com.example.ui.theme.ProkahRed
import com.example.ui.theme.ProkahTextPrimary
import com.example.ui.theme.ProkahTextSecondary
import java.text.DecimalFormat

@Composable
fun TapForBalanceContainer(
    balance: Double,
    isRevealed: Boolean,
    language: AppLanguage,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val decimalFormat = remember { DecimalFormat("#,##0.00") }
    val formattedBalance = decimalFormat.format(balance)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tap_for_balance_container")
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onTap),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, ProkahCardBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Red circle with Taka "৳" symbol
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ProkahRed),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "৳",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Animated text container
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    AnimatedContent(
                        targetState = isRevealed,
                        transitionSpec = {
                            if (targetState) {
                                (slideInHorizontally { width -> width / 3 } + fadeIn()) togetherWith
                                    (slideOutHorizontally { width -> -width / 3 } + fadeOut())
                            } else {
                                (slideInHorizontally { width -> -width / 3 } + fadeIn()) togetherWith
                                    (slideOutHorizontally { width -> width / 3 } + fadeOut())
                            }
                        },
                        label = "BalanceRevealAnimation"
                    ) { revealed ->
                        if (revealed) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = "৳ $formattedBalance",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProkahTextPrimary,
                                    modifier = Modifier.testTag("balance_amount_text")
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "(ট্যাপ করে লুকান)" else "(Tap to hide)",
                                    fontSize = 11.sp,
                                    color = ProkahTextSecondary
                                )
                            }
                        } else {
                            Text(
                                text = if (language == AppLanguage.BANGLA) "ব্যালেন্স জানতে ট্যাপ করুন" else "Tap for balance",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF555555),
                                modifier = Modifier.testTag("tap_to_view_text")
                            )
                        }
                    }
                }
            }

            // Subtle countdown indicator bar when revealed
            if (isRevealed) {
                var progress by remember { mutableFloatStateOf(1f) }
                val animatedProgress by animateFloatAsState(
                    targetValue = progress,
                    animationSpec = tween(durationMillis = 4000, easing = LinearEasing),
                    label = "ProgressAnimation"
                )

                LaunchedEffect(Unit) {
                    progress = 0f
                }

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = ProkahRed,
                    trackColor = ProkahCardBorder.copy(alpha = 0.5f)
                )
            }
        }
    }
}
