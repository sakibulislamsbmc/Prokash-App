package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.UserProfile
import com.example.ui.theme.ProkahCardBorder
import com.example.ui.theme.ProkahError
import com.example.ui.theme.ProkahOrange
import com.example.ui.theme.ProkahRed
import com.example.ui.theme.ProkahTextPrimary
import com.example.ui.theme.ProkahTextSecondary

@Composable
fun LoginScreen(
    userProfile: UserProfile,
    language: AppLanguage,
    onLogin: (String) -> Boolean,
    onBiometricLogin: () -> Unit,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun handleDigit(digit: String) {
        if (pin.length < 4) {
            val newPin = pin + digit
            pin = newPin
            errorMessage = null
            if (newPin.length == 4) {
                val success = onLogin(newPin)
                if (!success) {
                    errorMessage = if (language == AppLanguage.BANGLA) "ভুল পিন! আবার চেষ্টা করুন (ডিফল্ট: 1234)" else "Incorrect PIN! Try again (Default: 1234)"
                    pin = ""
                }
            }
        }
    }

    fun handleBackspace() {
        if (pin.isNotEmpty()) {
            pin = pin.dropLast(1)
            errorMessage = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF8F5))
            .testTag("login_screen")
    ) {
        // Language Toggle in Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onToggleLanguage() }
                    .testTag("login_lang_toggle"),
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ProkahCardBorder),
                shadowElevation = 1.dp
            ) {
                Text(
                    text = if (language == AppLanguage.BANGLA) "English" else "বাংলা",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProkahRed
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 80.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Prokah Logo & User Greeting
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Branded Emblem
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(ProkahOrange, ProkahRed)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "প্রো",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.BANGLA) "স্বাগতম, ${userProfile.name}" else "Welcome, ${userProfile.name}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProkahTextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userProfile.phoneNumber,
                    fontSize = 14.sp,
                    color = ProkahTextSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // PIN Dots
                Text(
                    text = if (language == AppLanguage.BANGLA) "আপনার ৪ ডিজিটের পিন দিন" else "Enter your 4-digit PIN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = ProkahTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        val isFilled = index < pin.length
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) ProkahRed else Color(0xFFDDD3CA))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                AnimatedVisibility(visible = errorMessage != null, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        text = errorMessage.orEmpty(),
                        color = ProkahError,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Keypad
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val rows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("bio", "0", "del")
                )

                for (row in rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (key in row) {
                            when (key) {
                                "bio" -> {
                                    Box(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .clip(CircleShape)
                                            .clickable { onBiometricLogin() }
                                            .testTag("biometric_login_btn"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Fingerprint,
                                            contentDescription = "Biometric Login",
                                            tint = ProkahRed,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                }
                                "del" -> {
                                    Box(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .clip(CircleShape)
                                            .clickable { handleBackspace() }
                                            .testTag("keypad_del"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                                            contentDescription = "Backspace",
                                            tint = ProkahTextPrimary,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                                else -> {
                                    Surface(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .clip(CircleShape)
                                            .clickable { handleDigit(key) }
                                            .testTag("keypad_$key"),
                                        shape = CircleShape,
                                        color = Color.White,
                                        shadowElevation = 1.dp
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = key,
                                                fontSize = 26.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = ProkahTextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Demo bypass button for test ease
                Button(
                    onClick = { onBiometricLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("demo_quick_login_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = ProkahRed
                    )
                ) {
                    Text(
                        text = if (language == AppLanguage.BANGLA) "দ্রুত লগইন করুন (ডেমো মোড)" else "Quick Login (Demo Mode)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
