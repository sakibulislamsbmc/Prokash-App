package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.ProkahOrange
import com.example.ui.theme.ProkahRed
import com.example.ui.theme.ProkahTextMuted
import com.example.ui.theme.ProkahTextPrimary

enum class BottomTab {
    HOME,
    TRANSACTIONS,
    CONTACTS,
    MY_PROKAH
}

@Composable
fun BottomNavBarWithQr(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    onQrClick: () -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_nav_bar")
    ) {
        // Elevated Bottom Bar Surface
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // 1. Home
                NavTabItem(
                    title = if (language == AppLanguage.BANGLA) "হোম" else "Home",
                    icon = Icons.Default.Whatshot,
                    isSelected = selectedTab == BottomTab.HOME,
                    onClick = { onTabSelected(BottomTab.HOME) },
                    testTag = "nav_tab_home",
                    modifier = Modifier.weight(1f)
                )

                // 2. Transactions
                NavTabItem(
                    title = if (language == AppLanguage.BANGLA) "লেনদেন" else "History",
                    icon = Icons.Default.ReceiptLong,
                    isSelected = selectedTab == BottomTab.TRANSACTIONS,
                    onClick = { onTabSelected(BottomTab.TRANSACTIONS) },
                    testTag = "nav_tab_transactions",
                    modifier = Modifier.weight(1f)
                )

                // Spacer for center QR button
                Box(modifier = Modifier.weight(1.2f))

                // 3. Contacts
                NavTabItem(
                    title = if (language == AppLanguage.BANGLA) "কন্টাক্টস" else "Contacts",
                    icon = Icons.Default.People,
                    isSelected = selectedTab == BottomTab.CONTACTS,
                    onClick = { onTabSelected(BottomTab.CONTACTS) },
                    testTag = "nav_tab_contacts",
                    modifier = Modifier.weight(1f)
                )

                // 4. My Prokah
                NavTabItem(
                    title = if (language == AppLanguage.BANGLA) "আমার প্রোকাহ" else "My Prokah",
                    icon = Icons.Default.Menu,
                    isSelected = selectedTab == BottomTab.MY_PROKAH,
                    onClick = { onTabSelected(BottomTab.MY_PROKAH) },
                    testTag = "nav_tab_my_prokah",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Overhanging Floating Center QR Button (matching screenshot)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-14).dp)
                .size(62.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(ProkahOrange, ProkahRed)
                    )
                )
                .clickable(onClick = onQrClick)
                .testTag("center_qr_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = "Scan QR Code",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
private fun NavTabItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val activeColor = ProkahRed
    val inactiveColor = Color(0xFF757575)

    Column(
        modifier = modifier
            .testTag(testTag)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(23.dp)
        )
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) activeColor else inactiveColor,
            maxLines = 1
        )
    }
}
