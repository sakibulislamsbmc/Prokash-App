package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ProkahRed

/**
 * High-fidelity vector icons matching the Bangladeshi MFS design system (Nagad/Prokah)
 */
@Composable
fun MfsIcon(
    name: String,
    modifier: Modifier = Modifier.size(32.dp),
    tint: Color = ProkahRed
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeWidth = 2.4.dp.toPx()
        val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)

        when (name) {
            "send_money" -> drawSendMoneyIcon(w, h, tint, stroke)
            "cash_out" -> drawCashOutIcon(w, h, tint, stroke)
            "mobile_recharge" -> drawMobileRechargeIcon(w, h, tint, stroke)
            "add_money" -> drawAddMoneyIcon(w, h, tint, stroke)
            "transfer_money" -> drawTransferMoneyIcon(w, h, tint, stroke)
            "bill_pay" -> drawBillPayIcon(w, h, tint, stroke)
            "emi_payment" -> drawEmiPaymentIcon(w, h, tint, stroke)
            "merchant_pay" -> drawMerchantPayIcon(w, h, tint, stroke)
            "toll" -> drawTollIcon(w, h, tint, stroke)
            "donation" -> drawDonationIcon(w, h, tint, stroke)
            "rapid_pass" -> drawRapidPassIcon(w, h, tint, stroke)
            "zakat" -> drawZakatIcon(w, h, tint, stroke)
            "more_services" -> drawMoreServicesIcon(w, h, tint, stroke)
            "contact_us" -> drawContactUsIcon(w, h, tint, stroke)
            "zakat_calc" -> drawZakatCalcIcon(w, h, tint, stroke)
            "limits_charges" -> drawLimitsChargesIcon(w, h, tint, stroke)
            "insurance" -> drawInsuranceIcon(w, h, tint, stroke)
            else -> drawSendMoneyIcon(w, h, tint, stroke)
        }
    }
}

// 1. Send Money: Circular curving arrow around Taka sign
private fun DrawScope.drawSendMoneyIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    // Circle arrow arc
    val path = Path().apply {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.15f, h * 0.15f, w * 0.85f, h * 0.85f),
            startAngleDegrees = -140f,
            sweepAngleDegrees = 290f,
            forceMoveTo = false
        )
    }
    drawPath(path, color, style = stroke)

    // Arrow head at end of arc
    val arrowPath = Path().apply {
        moveTo(w * 0.82f, h * 0.65f)
        lineTo(w * 0.95f, h * 0.60f)
        lineTo(w * 0.85f, h * 0.75f)
    }
    drawPath(arrowPath, color, style = stroke)

    // Taka '৳' glyph inside
    drawTakaGlyph(w * 0.5f, h * 0.5f, color, scale = 0.85f)
}

// 2. Cash Out: Banknotes with arrow pointing out
private fun DrawScope.drawCashOutIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    // Note outline
    val notePath = Path().apply {
        moveTo(w * 0.12f, h * 0.32f)
        lineTo(w * 0.80f, h * 0.32f)
        lineTo(w * 0.80f, h * 0.78f)
        lineTo(w * 0.12f, h * 0.78f)
        close()
    }
    drawPath(notePath, color, style = stroke)

    // Top folded bill edge
    val topEdge = Path().apply {
        moveTo(w * 0.25f, h * 0.24f)
        lineTo(w * 0.70f, h * 0.24f)
        lineTo(w * 0.80f, h * 0.32f)
    }
    drawPath(topEdge, color, style = stroke)

    // Outward arrow
    val arrow = Path().apply {
        moveTo(w * 0.75f, h * 0.55f)
        lineTo(w * 0.96f, h * 0.55f)
        moveTo(w * 0.88f, h * 0.44f)
        lineTo(w * 0.96f, h * 0.55f)
        lineTo(w * 0.88f, h * 0.66f)
    }
    drawPath(arrow, color, style = stroke)

    // ৳ mark inside note
    drawTakaGlyph(w * 0.46f, h * 0.55f, color, scale = 0.7f)
}

// 3. Mobile Recharge: Smartphone outline with lightning spark
private fun DrawScope.drawMobileRechargeIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    // Phone body
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.26f, h * 0.12f),
        size = Size(w * 0.48f, h * 0.76f),
        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
        style = stroke
    )
    // Speaker notch
    drawLine(
        color = color,
        start = Offset(w * 0.44f, h * 0.18f),
        end = Offset(w * 0.56f, h * 0.18f),
        strokeWidth = stroke.width
    )
    // Lightning bolt in center
    val bolt = Path().apply {
        moveTo(w * 0.52f, h * 0.32f)
        lineTo(w * 0.42f, h * 0.50f)
        lineTo(w * 0.52f, h * 0.50f)
        lineTo(w * 0.46f, h * 0.68f)
        lineTo(w * 0.58f, h * 0.46f)
        lineTo(w * 0.48f, h * 0.46f)
        close()
    }
    drawPath(bolt, color)
}

// 4. Add Money: Wallet with plus badge
private fun DrawScope.drawAddMoneyIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    // Wallet body
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.12f, h * 0.28f),
        size = Size(w * 0.74f, h * 0.56f),
        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
        style = stroke
    )
    // Wallet flap clip
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.60f, h * 0.46f),
        size = Size(w * 0.26f, h * 0.20f),
        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
        style = stroke
    )
    drawCircle(color, radius = 2.5.dp.toPx(), center = Offset(w * 0.72f, h * 0.56f))

    // Plus sign on top right
    val plusSize = 7.dp.toPx()
    val plusCenter = Offset(w * 0.74f, h * 0.22f)
    drawLine(color, plusCenter.copy(x = plusCenter.x - plusSize), plusCenter.copy(x = plusCenter.x + plusSize), strokeWidth = stroke.width)
    drawLine(color, plusCenter.copy(y = plusCenter.y - plusSize), plusCenter.copy(y = plusCenter.y + plusSize), strokeWidth = stroke.width)
}

// 5. Transfer Money: Two circular arrows with ৳
private fun DrawScope.drawTransferMoneyIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    // Arc 1 (Top-Right)
    val arc1 = Path().apply {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.14f, h * 0.14f, w * 0.86f, h * 0.86f),
            startAngleDegrees = -120f,
            sweepAngleDegrees = 110f,
            forceMoveTo = false
        )
    }
    drawPath(arc1, color, style = stroke)
    // Arrow 1
    val arrow1 = Path().apply {
        moveTo(w * 0.78f, h * 0.38f)
        lineTo(w * 0.88f, h * 0.40f)
        lineTo(w * 0.88f, h * 0.26f)
    }
    drawPath(arrow1, color, style = stroke)

    // Arc 2 (Bottom-Left)
    val arc2 = Path().apply {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.14f, h * 0.14f, w * 0.86f, h * 0.86f),
            startAngleDegrees = 60f,
            sweepAngleDegrees = 110f,
            forceMoveTo = false
        )
    }
    drawPath(arc2, color, style = stroke)
    // Arrow 2
    val arrow2 = Path().apply {
        moveTo(w * 0.22f, h * 0.62f)
        lineTo(w * 0.12f, h * 0.60f)
        lineTo(w * 0.12f, h * 0.74f)
    }
    drawPath(arrow2, color, style = stroke)

    drawTakaGlyph(w * 0.5f, h * 0.5f, color, scale = 0.8f)
}

// 6. Bill Pay: Receipt / invoice document with lines and ৳
private fun DrawScope.drawBillPayIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    // Document
    val doc = Path().apply {
        moveTo(w * 0.20f, h * 0.15f)
        lineTo(w * 0.65f, h * 0.15f)
        lineTo(w * 0.80f, h * 0.30f)
        lineTo(w * 0.80f, h * 0.85f)
        lineTo(w * 0.20f, h * 0.85f)
        close()
    }
    drawPath(doc, color, style = stroke)
    // Fold
    val fold = Path().apply {
        moveTo(w * 0.65f, h * 0.15f)
        lineTo(w * 0.65f, h * 0.30f)
        lineTo(w * 0.80f, h * 0.30f)
    }
    drawPath(fold, color, style = stroke)

    // Content lines
    drawLine(color, Offset(w * 0.32f, h * 0.42f), Offset(w * 0.68f, h * 0.42f), strokeWidth = stroke.width * 0.75f)
    drawLine(color, Offset(w * 0.32f, h * 0.56f), Offset(w * 0.52f, h * 0.56f), strokeWidth = stroke.width * 0.75f)

    // ৳ coin on bottom right
    drawCircle(color, radius = 5.dp.toPx(), center = Offset(w * 0.70f, h * 0.74f), style = stroke)
    drawTakaGlyph(w * 0.70f, h * 0.74f, color, scale = 0.45f)
}

// 7. EMI Payment: Calculator with %
private fun DrawScope.drawEmiPaymentIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.22f, h * 0.15f),
        size = Size(w * 0.56f, h * 0.72f),
        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
        style = stroke
    )
    // Screen with %
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.32f, h * 0.25f),
        size = Size(w * 0.36f, h * 0.20f),
        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
        style = stroke
    )
    // Grid dots
    val dotR = 2.dp.toPx()
    drawCircle(color, dotR, Offset(w * 0.36f, h * 0.56f))
    drawCircle(color, dotR, Offset(w * 0.50f, h * 0.56f))
    drawCircle(color, dotR, Offset(w * 0.64f, h * 0.56f))
    drawCircle(color, dotR, Offset(w * 0.36f, h * 0.70f))
    drawCircle(color, dotR, Offset(w * 0.50f, h * 0.70f))
    drawCircle(color, dotR, Offset(w * 0.64f, h * 0.70f))
}

// 8. Merchant Pay: Shopping bag with QR outline
private fun DrawScope.drawMerchantPayIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    // Bag body
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.20f, h * 0.32f),
        size = Size(w * 0.60f, h * 0.58f),
        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
        style = stroke
    )
    // Bag handle
    val handle = Path().apply {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.36f, h * 0.12f, w * 0.64f, h * 0.40f),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
    }
    drawPath(handle, color, style = stroke)

    // QR grid in bag
    val qrR = 2.5.dp.toPx()
    drawRoundRect(color, Offset(w * 0.32f, h * 0.46f), Size(10f, 10f), CornerRadius(1f, 1f))
    drawRoundRect(color, Offset(w * 0.56f, h * 0.46f), Size(10f, 10f), CornerRadius(1f, 1f))
    drawRoundRect(color, Offset(w * 0.32f, h * 0.70f), Size(10f, 10f), CornerRadius(1f, 1f))
    drawRoundRect(color, Offset(w * 0.56f, h * 0.70f), Size(10f, 10f), CornerRadius(1f, 1f))
}

// 9. Toll Icon
private fun DrawScope.drawTollIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    // Barrier post
    drawLine(color, Offset(w * 0.22f, h * 0.25f), Offset(w * 0.22f, h * 0.85f), strokeWidth = stroke.width * 1.2f)
    // Barrier arm
    drawLine(color, Offset(w * 0.20f, h * 0.35f), Offset(w * 0.82f, h * 0.28f), strokeWidth = stroke.width)

    // Car outline below barrier
    val car = Path().apply {
        moveTo(w * 0.36f, h * 0.65f)
        lineTo(w * 0.44f, h * 0.50f)
        lineTo(w * 0.70f, h * 0.50f)
        lineTo(w * 0.78f, h * 0.65f)
        lineTo(w * 0.86f, h * 0.68f)
        lineTo(w * 0.86f, h * 0.82f)
        lineTo(w * 0.28f, h * 0.82f)
        lineTo(w * 0.28f, h * 0.68f)
        close()
    }
    drawPath(car, color, style = stroke)
}

// 10. Donation Icon
private fun DrawScope.drawDonationIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    // Giving hand
    val hand = Path().apply {
        moveTo(w * 0.15f, h * 0.60f)
        lineTo(w * 0.45f, h * 0.60f)
        lineTo(w * 0.62f, h * 0.52f)
        lineTo(w * 0.75f, h * 0.58f)
        lineTo(w * 0.58f, h * 0.74f)
        lineTo(w * 0.15f, h * 0.74f)
        close()
    }
    drawPath(hand, color, style = stroke)

    // Heart above hand
    val heart = Path().apply {
        moveTo(w * 0.52f, h * 0.40f)
        cubicTo(w * 0.42f, h * 0.25f, w * 0.30f, h * 0.34f, w * 0.52f, h * 0.50f)
        cubicTo(w * 0.74f, h * 0.34f, w * 0.62f, h * 0.25f, w * 0.52f, h * 0.40f)
        close()
    }
    drawPath(heart, color)
}

// 11. Rapid Pass (Metro Train)
private fun DrawScope.drawRapidPassIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    val train = Path().apply {
        moveTo(w * 0.15f, h * 0.72f)
        lineTo(w * 0.50f, h * 0.72f)
        quadraticTo(w * 0.85f, h * 0.72f, w * 0.88f, h * 0.56f)
        quadraticTo(w * 0.85f, h * 0.40f, w * 0.50f, h * 0.40f)
        lineTo(w * 0.15f, h * 0.40f)
        close()
    }
    drawPath(train, color, style = stroke)
    // Window
    drawRoundRect(color, Offset(w * 0.28f, h * 0.46f), Size(w * 0.35f, h * 0.14f), CornerRadius(2f, 2f), style = stroke)
    // Speed lines below
    drawLine(color, Offset(w * 0.12f, h * 0.80f), Offset(w * 0.78f, h * 0.80f), strokeWidth = stroke.width)
}

// 12. Zakat Icon: Money bag with ৳
private fun DrawScope.drawZakatIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    val bag = Path().apply {
        moveTo(w * 0.38f, h * 0.34f)
        lineTo(w * 0.32f, h * 0.22f)
        lineTo(w * 0.68f, h * 0.22f)
        lineTo(w * 0.62f, h * 0.34f)
        cubicTo(w * 0.86f, h * 0.44f, w * 0.86f, h * 0.82f, w * 0.50f, h * 0.86f)
        cubicTo(w * 0.14f, h * 0.82f, w * 0.14f, h * 0.44f, w * 0.38f, h * 0.34f)
        close()
    }
    drawPath(bag, color, style = stroke)
    // Neck tie
    drawLine(color, Offset(w * 0.36f, h * 0.34f), Offset(w * 0.64f, h * 0.34f), strokeWidth = stroke.width * 1.3f)
    drawTakaGlyph(w * 0.5f, h * 0.60f, color, scale = 0.75f)
}

// 13. More Services Icon: 4 squares + plus
private fun DrawScope.drawMoreServicesIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    val s = 11.dp.toPx()
    val r = 2.dp.toPx()
    drawRoundRect(color, Offset(w * 0.24f, h * 0.24f), Size(s, s), CornerRadius(r, r), style = stroke)
    drawRoundRect(color, Offset(w * 0.56f, h * 0.24f), Size(s, s), CornerRadius(r, r), style = stroke)
    drawRoundRect(color, Offset(w * 0.24f, h * 0.56f), Size(s, s), CornerRadius(r, r), style = stroke)
    // Bottom right with plus
    drawRoundRect(color, Offset(w * 0.56f, h * 0.56f), Size(s, s), CornerRadius(r, r), style = stroke)
    drawLine(color, Offset(w * 0.62f, h * 0.70f), Offset(w * 0.76f, h * 0.70f), strokeWidth = stroke.width)
    drawLine(color, Offset(w * 0.69f, h * 0.63f), Offset(w * 0.69f, h * 0.77f), strokeWidth = stroke.width)
}

// 14. Contact Us Icon
private fun DrawScope.drawContactUsIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    val pin = Path().apply {
        moveTo(w * 0.50f, h * 0.86f)
        cubicTo(w * 0.26f, h * 0.58f, w * 0.24f, h * 0.20f, w * 0.50f, h * 0.20f)
        cubicTo(w * 0.76f, h * 0.20f, w * 0.74f, h * 0.58f, w * 0.50f, h * 0.86f)
        close()
    }
    drawPath(pin, color, style = stroke)
    drawCircle(color, radius = 3.dp.toPx(), center = Offset(w * 0.50f, h * 0.42f))
}

// 15. Zakat Calc Icon: Mosque dome with calc
private fun DrawScope.drawZakatCalcIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    val dome = Path().apply {
        moveTo(w * 0.24f, h * 0.85f)
        lineTo(w * 0.24f, h * 0.48f)
        cubicTo(w * 0.24f, h * 0.22f, w * 0.50f, h * 0.12f, w * 0.50f, h * 0.12f)
        cubicTo(w * 0.50f, h * 0.12f, w * 0.76f, h * 0.22f, w * 0.76f, h * 0.48f)
        lineTo(w * 0.76f, h * 0.85f)
        close()
    }
    drawPath(dome, color, style = stroke)
    // Calculator grid in dome
    val dotR = 1.5.dp.toPx()
    drawCircle(color, dotR, Offset(w * 0.40f, h * 0.55f))
    drawCircle(color, dotR, Offset(w * 0.60f, h * 0.55f))
    drawCircle(color, dotR, Offset(w * 0.40f, h * 0.70f))
    drawCircle(color, dotR, Offset(w * 0.60f, h * 0.70f))
}

// 16. Limits & Charges
private fun DrawScope.drawLimitsChargesIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.22f, h * 0.18f),
        size = Size(w * 0.56f, h * 0.66f),
        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
        style = stroke
    )
    // Plus, minus, multiply, ৳
    drawLine(color, Offset(w * 0.32f, h * 0.36f), Offset(w * 0.46f, h * 0.36f), strokeWidth = stroke.width)
    drawLine(color, Offset(w * 0.39f, h * 0.29f), Offset(w * 0.39f, h * 0.43f), strokeWidth = stroke.width)

    drawLine(color, Offset(w * 0.56f, h * 0.36f), Offset(w * 0.70f, h * 0.36f), strokeWidth = stroke.width)

    drawTakaGlyph(w * 0.63f, h * 0.64f, color, scale = 0.55f)
}

// 17. Insurance Policy (Umbrella)
private fun DrawScope.drawInsuranceIcon(w: Float, h: Float, color: Color, stroke: Stroke) {
    val umbrella = Path().apply {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.18f, h * 0.18f, w * 0.82f, h * 0.70f),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        lineTo(w * 0.82f, h * 0.46f)
        lineTo(w * 0.18f, h * 0.46f)
        close()
    }
    drawPath(umbrella, color, style = stroke)
    // Umbrella stem and handle
    val handle = Path().apply {
        moveTo(w * 0.50f, h * 0.46f)
        lineTo(w * 0.50f, h * 0.76f)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.38f, h * 0.68f, w * 0.50f, h * 0.86f),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
    }
    drawPath(handle, color, style = stroke)
}

// Helper to draw clean stylized Taka glyph '৳'
private fun DrawScope.drawTakaGlyph(cx: Float, cy: Float, color: Color, scale: Float) {
    val s = 10f * scale
    val path = Path().apply {
        moveTo(cx - (s * 0.6f), cy - (s * 0.8f))
        lineTo(cx + (s * 0.6f), cy - (s * 0.8f))
        lineTo(cx + (s * 0.6f), cy - (s * 0.2f))
        cubicTo(cx + (s * 0.6f), cy + (s * 0.8f), cx - (s * 0.6f), cy + (s * 0.8f), cx - (s * 0.6f), cy + (s * 0.1f))
        cubicTo(cx - (s * 0.6f), cy - (s * 0.5f), cx + (s * 0.3f), cy - (s * 0.5f), cx + (s * 0.3f), cy + (s * 0.1f))
    }
    drawPath(path, color, style = Stroke(width = 2f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round))
}
