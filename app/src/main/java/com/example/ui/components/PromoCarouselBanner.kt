package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.ProkahRed
import kotlinx.coroutines.delay

data class PromoSlide(
    val titleBn: String,
    val titleEn: String,
    val highlightBn: String,
    val highlightEn: String,
    val gradientColors: List<Color>,
    val badgeBn: String,
    val badgeEn: String
)

@Composable
fun PromoCarouselBanner(
    language: AppLanguage,
    onPromoClick: (PromoSlide) -> Unit,
    modifier: Modifier = Modifier
) {
    val slides = remember {
        listOf(
            PromoSlide(
                titleBn = "প্রোকাহ অ্যাপে\nনতুন Experience",
                titleEn = "New Experience in\nProkah App",
                highlightBn = "সব হবে Nonstop",
                highlightEn = "Always Nonstop",
                gradientColors = listOf(Color(0xFFBA1A1A), Color(0xFFE52E2D), Color(0xFFF57C00)),
                badgeBn = "নতুন অফার",
                badgeEn = "New Offer"
            ),
            PromoSlide(
                titleBn = "১.২৫% ক্যাশ আউট চার্জ\nসবচেয়ে কম রেট!",
                titleEn = "1.25% Cash Out Charge\nLowest in Market!",
                highlightBn = "প্রতি হাজারে মাত্র ১২.৫০৳",
                highlightEn = "Only ৳12.50 per 1k",
                gradientColors = listOf(Color(0xFFC2185B), Color(0xFFE91E63), Color(0xFFFF5722)),
                badgeBn = "সবার সেরা চার্জ",
                badgeEn = "Best Rate"
            ),
            PromoSlide(
                titleBn = "ফ্রি সেন্ড মানি\nবন্ধুদের যেকোনো নম্বরে",
                titleEn = "Free Send Money\nTo Any Prokah Number",
                highlightBn = "খরচ ৳০.০০",
                highlightEn = "Cost ৳0.00",
                gradientColors = listOf(Color(0xFFE65100), Color(0xFFFF9800), Color(0xFFFFC107)),
                badgeBn = "জিরো ফি",
                badgeEn = "Zero Fee"
            ),
            PromoSlide(
                titleBn = "বিদ্যুৎ, গ্যাস ও পানির বিল\nসহজে পরিশোধ করুন",
                titleEn = "Pay Electricity & Gas\nBills Instantly",
                highlightBn = "ঘরে বসেই পে বিল",
                highlightEn = "Pay from Home",
                gradientColors = listOf(Color(0xFFBF360C), Color(0xFFD84315), Color(0xFFFF7043)),
                badgeBn = "সহজ বিল পে",
                badgeEn = "Instant Bill"
            )
        )
    }

    var currentSlideIndex by remember { mutableIntStateOf(0) }

    // Auto rotate every 4.5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(4500)
            currentSlideIndex = (currentSlideIndex + 1) % slides.size
        }
    }

    val currentSlide = slides[currentSlideIndex]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("promo_carousel_banner")
    ) {
        // Main Banner Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(3.dp, RoundedCornerShape(16.dp))
                .clickable {
                    onPromoClick(currentSlide)
                    currentSlideIndex = (currentSlideIndex + 1) % slides.size
                }
        ) {
            AnimatedContent(
                targetState = currentSlide,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "PromoSlideAnimation"
            ) { slide ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = slide.gradientColors
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    // Decorative glow circles
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .align(Alignment.CenterEnd)
                            .background(Color.White.copy(alpha = 0.08f), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.CenterEnd)
                            .padding(end = 20.dp)
                            .background(Color.White.copy(alpha = 0.12f), CircleShape)
                    )

                    // Text Content (Left side)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.68f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.25f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.BANGLA) slide.badgeBn else slide.badgeEn,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (language == AppLanguage.BANGLA) slide.titleBn else slide.titleEn,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (language == AppLanguage.BANGLA) slide.highlightBn else slide.highlightEn,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFFFEB3B)
                        )
                    }

                    // 3D Phone & Fintech elements mockup (Right side)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(width = 88.dp, height = 104.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.95f))
                            .padding(5.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Mini notch
                            Box(
                                modifier = Modifier
                                    .width(22.dp)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color(0xFF333333))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Mini Prokah app mockup
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ProkahRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("৳ 24,850", color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            // Mini grid cards
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(modifier = Modifier.size(14.dp).background(Color(0xFFFFEBEE), CircleShape))
                                Box(modifier = Modifier.size(14.dp).background(Color(0xFFFFEBEE), CircleShape))
                                Box(modifier = Modifier.size(14.dp).background(Color(0xFFFFEBEE), CircleShape))
                                Box(modifier = Modifier.size(14.dp).background(Color(0xFFFFEBEE), CircleShape))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(modifier = Modifier.size(14.dp).background(Color(0xFFFFEBEE), CircleShape))
                                Box(modifier = Modifier.size(14.dp).background(Color(0xFFFFEBEE), CircleShape))
                                Box(modifier = Modifier.size(14.dp).background(Color(0xFFFFEBEE), CircleShape))
                                Box(modifier = Modifier.size(14.dp).background(Color(0xFFFFEBEE), CircleShape))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0xFFE52E2D).copy(alpha = 0.85f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("PROKAH", color = Color.White, fontSize = 6.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Pagination Dots (matching screenshot)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            slides.indices.forEach { index ->
                val isSelected = index == currentSlideIndex
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) ProkahRed else Color(0xFFCFD8DC)
                        )
                        .clickable { currentSlideIndex = index }
                )
            }
        }
    }
}
