package com.example.ui.screens

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
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.UserProfile
import com.example.ui.components.MfsIcon
import com.example.ui.components.PromoCarouselBanner
import com.example.ui.components.ScenicHeaderBackground
import com.example.ui.components.ServiceCardContainer
import com.example.ui.components.TapForBalanceContainer
import com.example.ui.theme.ProkahCardBorder
import com.example.ui.theme.ProkahOrange
import com.example.ui.theme.ProkahRed
import com.example.ui.theme.ProkahTextPrimary
import com.example.ui.theme.ProkahTextSecondary
import com.example.viewmodel.ActiveModal
import java.util.Calendar

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    isBalanceRevealed: Boolean,
    language: AppLanguage,
    onTapBalance: () -> Unit,
    onOpenModal: (ActiveModal) -> Unit,
    onToggleLanguage: () -> Unit,
    unreadNotificationCount: Int,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    // Dynamic greeting based on current time
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        currentHour in 5..11 -> if (language == AppLanguage.BANGLA) "শুভ সকাল" else "Good Morning"
        currentHour in 12..15 -> if (language == AppLanguage.BANGLA) "শুভ দুপুর" else "Good Afternoon"
        currentHour in 16..18 -> if (language == AppLanguage.BANGLA) "শুভ বিকাল" else "Good Afternoon"
        currentHour in 19..23 -> if (language == AppLanguage.BANGLA) "শুভ সন্ধ্যা" else "Good Evening"
        else -> if (language == AppLanguage.BANGLA) "শুভ রাত্রি" else "Good Night"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F7F5))
            .testTag("home_screen"),
        contentPadding = PaddingValues(
            top = 0.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp
        )
    ) {
        // 1. Header with Scenic Background and Top Profile Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                // Scenic river bridge & village background
                ScenicHeaderBackground(modifier = Modifier.matchParentSize())

                // Top Bar Profile & Actions
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 44.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left: Logo and Greeting + Name
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Prokah Flame Runner Logo
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .shadow(4.dp, CircleShape)
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
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = greeting,
                                    fontSize = 13.sp,
                                    color = Color(0xFF616161)
                                )
                                Text(
                                    text = userProfile.name,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProkahTextPrimary
                                )
                            }
                        }

                        // Right: Language Switcher and Notification Bell with "99+" badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Language Switch Chip
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { onToggleLanguage() }
                                    .testTag("home_lang_toggle"),
                                color = Color.White.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ProkahCardBorder)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.BANGLA) "EN" else "বাং",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProkahRed
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // Bell with "99+" Red Badge
                            IconButton(
                                onClick = { onOpenModal(ActiveModal.NOTIFICATIONS) },
                                modifier = Modifier.testTag("notification_bell_btn")
                            ) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = ProkahRed,
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = if (unreadNotificationCount > 99) "99+" else unreadNotificationCount.toString(),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = "Notifications",
                                        tint = Color(0xFF262626),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // "Tap for Balance" pill container
                    TapForBalanceContainer(
                        balance = userProfile.balance,
                        isRevealed = isBalanceRevealed,
                        language = language,
                        onTap = onTapBalance
                    )
                }
            }
        }

        // 2. Primary 4x2 Grid of Services (Matching Screenshot 1)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ServiceCardContainer(
                        iconName = "send_money",
                        title = if (language == AppLanguage.BANGLA) "সেন্ড\nমানি" else "Send\nMoney",
                        onClick = { onOpenModal(ActiveModal.SEND_MONEY) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "cash_out",
                        title = if (language == AppLanguage.BANGLA) "ক্যাশ\nআউট" else "Cash\nOut",
                        onClick = { onOpenModal(ActiveModal.CASH_OUT) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "mobile_recharge",
                        title = if (language == AppLanguage.BANGLA) "মোবাইল\nরিচার্জ" else "Mobile\nRecharge",
                        onClick = { onOpenModal(ActiveModal.MOBILE_RECHARGE) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "add_money",
                        title = if (language == AppLanguage.BANGLA) "অ্যাড\nমানি" else "Add\nMoney",
                        onClick = { onOpenModal(ActiveModal.ADD_MONEY) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ServiceCardContainer(
                        iconName = "transfer_money",
                        title = if (language == AppLanguage.BANGLA) "ট্রান্সফার\nমানি" else "Transfer\nMoney",
                        onClick = { onOpenModal(ActiveModal.TRANSFER_MONEY) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "bill_pay",
                        title = if (language == AppLanguage.BANGLA) "বিল\nপে" else "Bill\nPay",
                        onClick = { onOpenModal(ActiveModal.BILL_PAY) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "emi_payment",
                        title = if (language == AppLanguage.BANGLA) "ইএমআই\nপেমেন্ট" else "EMI\nPayment",
                        onClick = { onOpenModal(ActiveModal.BILL_PAY) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "merchant_pay",
                        title = if (language == AppLanguage.BANGLA) "মার্চেন্ট\nপে" else "Merchant\nPay",
                        onClick = { onOpenModal(ActiveModal.MERCHANT_PAY) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. Promotional Carousel Banner (Matching Screenshot 1 & 2)
        item {
            PromoCarouselBanner(
                language = language,
                onPromoClick = { onOpenModal(ActiveModal.MOBILE_RECHARGE) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            )
        }

        // 4. Secondary Services ("সার্ভিস সমূহ") (Matching Screenshot 2)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.BANGLA) "সার্ভিস সমূহ" else "Services",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF424242),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Row 1 of Secondary Services (Toll, Donation, Rapid Pass, Zakat)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ServiceCardContainer(
                        iconName = "toll",
                        title = if (language == AppLanguage.BANGLA) "টোল" else "Toll",
                        onClick = { onOpenModal(ActiveModal.BILL_PAY) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "donation",
                        title = if (language == AppLanguage.BANGLA) "ডোনেশন" else "Donation",
                        onClick = { onOpenModal(ActiveModal.SEND_MONEY) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "rapid_pass",
                        title = if (language == AppLanguage.BANGLA) "র‍্যাপিড\nপাস" else "Rapid\nPass",
                        onClick = { onOpenModal(ActiveModal.BILL_PAY) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "zakat",
                        title = if (language == AppLanguage.BANGLA) "যাকাত" else "Zakat",
                        onClick = { onOpenModal(ActiveModal.ZAKAT_CALCULATOR) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Row 2: Other Services card (matching single card on row 2 in screenshot 2)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ServiceCardContainer(
                        iconName = "more_services",
                        title = if (language == AppLanguage.BANGLA) "অন্যান্য\nসেবাসমূহ" else "Other\nServices",
                        onClick = { onOpenModal(ActiveModal.BILL_PAY) },
                        modifier = Modifier.weight(1f)
                    )
                    // Invisible placeholders to align perfectly in 4-column grid
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // 5. "অন্যান্য" (Others) Section (Matching Screenshot 2)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 18.dp, bottom = 12.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.BANGLA) "অন্যান্য" else "Others",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF424242),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ServiceCardContainer(
                        iconName = "contact_us",
                        title = if (language == AppLanguage.BANGLA) "যোগাযোগ\nকরুন" else "Contact\nUs",
                        onClick = { onOpenModal(ActiveModal.CONTACT_US) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "zakat_calc",
                        title = if (language == AppLanguage.BANGLA) "যাকাত\nক্যালকুলেটর" else "Zakat\nCalculator",
                        onClick = { onOpenModal(ActiveModal.ZAKAT_CALCULATOR) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "limits_charges",
                        title = if (language == AppLanguage.BANGLA) "লিমিট\nও চার্জ" else "Limits\n& Charge",
                        onClick = { onOpenModal(ActiveModal.LIMITS_AND_CHARGES) },
                        modifier = Modifier.weight(1f)
                    )
                    ServiceCardContainer(
                        iconName = "insurance",
                        title = if (language == AppLanguage.BANGLA) "ইন্সুরেন্স\nপলিসি" else "Insurance\nPolicy",
                        onClick = { onOpenModal(ActiveModal.CONTACT_US) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
