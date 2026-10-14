package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.model.UserProfile
import com.example.ui.theme.ProkahCardBorder
import com.example.ui.theme.ProkahError
import com.example.ui.theme.ProkahOrange
import com.example.ui.theme.ProkahRed
import com.example.ui.theme.ProkahSuccess
import com.example.ui.theme.ProkahTextPrimary
import com.example.ui.theme.ProkahTextSecondary
import com.example.viewmodel.ActiveModal
import java.text.DecimalFormat

@Composable
fun MyProkahScreen(
    userProfile: UserProfile,
    language: AppLanguage,
    onToggleLanguage: () -> Unit,
    onOpenModal: (ActiveModal) -> Unit,
    onLogout: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val decimalFormat = remember { DecimalFormat("#,##0.00") }
    var biometricEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F7F5))
            .testTag("my_prokah_screen"),
        contentPadding = PaddingValues(
            top = 44.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Profile Header Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, ProkahCardBorder),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(ProkahRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProfile.name.take(1).uppercase(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userProfile.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProkahTextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                // KYC Verified Badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFE8F5E9))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, null, tint = ProkahSuccess, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = if (language == AppLanguage.BANGLA) "ভেরিফায়েড" else "Verified",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ProkahSuccess
                                        )
                                    }
                                }
                            }

                            Text(
                                text = userProfile.phoneNumber,
                                fontSize = 14.sp,
                                color = ProkahTextSecondary
                            )
                            Text(
                                text = "A/C: ${userProfile.accountNumber}",
                                fontSize = 12.sp,
                                color = Color(0xFF757575)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Daily Limit Progress
                    val usedDaily = userProfile.dailyLimitTotal - userProfile.dailyLimitRemaining
                    val dailyProgress = (usedDaily / userProfile.dailyLimitTotal).toFloat()

                    Text(
                        text = if (language == AppLanguage.BANGLA) "আজকের লেনদেন লিমিট" else "Daily Transfer Limit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ProkahTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { dailyProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = ProkahRed,
                        trackColor = Color(0xFFFFECEB)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ব্যবহৃত: ৳${decimalFormat.format(usedDaily)}",
                            fontSize = 11.sp,
                            color = ProkahTextSecondary
                        )
                        Text(
                            text = "অবশিষ্ট: ৳${decimalFormat.format(userProfile.dailyLimitRemaining)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ProkahSuccess
                        )
                    }
                }
            }
        }

        // Settings & Features Group
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, ProkahCardBorder)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Language,
                        title = if (language == AppLanguage.BANGLA) "ভাষা পরিবর্তন (Language)" else "Change Language",
                        subtitle = if (language == AppLanguage.BANGLA) "বর্তমান: বাংলা" else "Current: English",
                        onClick = onToggleLanguage
                    )

                    SettingsRowItem(
                        icon = Icons.Default.Speed,
                        title = if (language == AppLanguage.BANGLA) "লিমিট ও চার্জ সমূহ" else "Limits & Charges",
                        subtitle = if (language == AppLanguage.BANGLA) "দৈনিক ও মাসিক ব্যবহারের তথ্য" else "Daily & Monthly fee schedule",
                        onClick = { onOpenModal(ActiveModal.LIMITS_AND_CHARGES) }
                    )

                    SettingsRowItem(
                        icon = Icons.Default.Calculate,
                        title = if (language == AppLanguage.BANGLA) "যাকাত ক্যালকুলেটর" else "Zakat Calculator",
                        subtitle = if (language == AppLanguage.BANGLA) "সহজে নিসাব ও যাকাত হিসাব করুন" else "Calculate Nisab & Zakat",
                        onClick = { onOpenModal(ActiveModal.ZAKAT_CALCULATOR) }
                    )

                    SettingsRowItem(
                        icon = Icons.Default.Lock,
                        title = if (language == AppLanguage.BANGLA) "পিন পরিবর্তন করুন" else "Change PIN",
                        subtitle = if (language == AppLanguage.BANGLA) "ডিফল্ট পিন: 1234" else "Default PIN: 1234",
                        onClick = { onOpenModal(ActiveModal.CONTACT_US) }
                    )

                    // Biometric Toggle Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF3E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Fingerprint, null, tint = ProkahOrange, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.BANGLA) "বায়োমেট্রিক লগইন" else "Biometric Login",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ProkahTextPrimary
                            )
                            Text(
                                text = if (language == AppLanguage.BANGLA) "আঙুলের ছাপ দিয়ে সহজে লগইন" else "Fingerprint / Face ID",
                                fontSize = 12.sp,
                                color = ProkahTextSecondary
                            )
                        }
                        Switch(
                            checked = biometricEnabled,
                            onCheckedChange = { biometricEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = ProkahRed, checkedTrackColor = Color(0xFFFFECEB))
                        )
                    }

                    SettingsRowItem(
                        icon = Icons.Default.HeadsetMic,
                        title = if (language == AppLanguage.BANGLA) "সাহায্য ও যোগাযোগ" else "Help & Support",
                        subtitle = "২৪/৭ হেল্পলাইন: ১৬১৬৭",
                        onClick = { onOpenModal(ActiveModal.CONTACT_US) }
                    )
                }
            }
        }

        // Logout Button
        item {
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("logout_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEE),
                    contentColor = ProkahError
                )
            ) {
                Icon(Icons.Default.Logout, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.BANGLA) "লগ আউট করুন" else "Log Out",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SettingsRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFECEB)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = ProkahRed, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ProkahTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = ProkahTextSecondary
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFFBDBDBD),
            modifier = Modifier.size(14.dp)
        )
    }
}
