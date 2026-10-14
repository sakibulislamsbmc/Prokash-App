package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp

@Composable
fun ScenicHeaderBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            // 1. Sky warm watercolor gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFE0B2).copy(alpha = 0.6f),
                        Color(0xFFFFF3E0).copy(alpha = 0.8f),
                        Color(0xFFFAF3EC),
                        Color(0xFFFFFFFF)
                    ),
                    startY = 0f,
                    endY = height
                ),
                size = size
            )

            // 2. Far monument silhouette / distant skyline on the left (National Memorial / bridge towers)
            val monumentPath = Path().apply {
                moveTo(width * 0.10f, height * 0.70f)
                lineTo(width * 0.12f, height * 0.40f)
                lineTo(width * 0.14f, height * 0.70f)
                close()
            }
            drawPath(
                path = monumentPath,
                color = Color(0xFFC7B299).copy(alpha = 0.5f)
            )

            // 3. Iconic Padma / Jamuna river bridge arches across the river
            val bridgeY = height * 0.66f
            val bridgeHeight = 8f
            // Bridge deck
            drawRect(
                color = Color(0xFFA1887F).copy(alpha = 0.65f),
                topLeft = Offset(width * 0.08f, bridgeY),
                size = Size(width * 0.85f, bridgeHeight)
            )

            // Bridge piers and truss arches
            val numPillars = 8
            val pillarSpacing = (width * 0.65f) / numPillars
            val startPillarX = width * 0.12f
            for (i in 0..numPillars) {
                val px = startPillarX + (i * pillarSpacing)
                // Pier
                drawRect(
                    color = Color(0xFF8D6E63).copy(alpha = 0.7f),
                    topLeft = Offset(px - 3f, bridgeY),
                    size = Size(6f, height * 0.14f)
                )
                // Arch between pillars
                if (i < numPillars) {
                    val archPath = Path().apply {
                        moveTo(px, bridgeY)
                        quadraticTo(
                            px + pillarSpacing / 2f,
                            bridgeY - 14f,
                            px + pillarSpacing,
                            bridgeY
                        )
                    }
                    drawPath(
                        path = archPath,
                        color = Color(0xFF8D6E63).copy(alpha = 0.5f)
                    )
                }
            }

            // 4. Calm River water gradient
            val riverY = height * 0.72f
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFD7CCC8).copy(alpha = 0.35f),
                        Color(0xFFCFD8DC).copy(alpha = 0.45f),
                        Color(0xFFB0BEC5).copy(alpha = 0.25f)
                    ),
                    startY = riverY,
                    endY = height
                ),
                topLeft = Offset(0f, riverY),
                size = Size(width, height - riverY)
            )

            // Gentle river wave ripples
            val wavePath = Path().apply {
                moveTo(0f, riverY + 12f)
                quadraticTo(width * 0.25f, riverY + 8f, width * 0.5f, riverY + 12f)
                quadraticTo(width * 0.75f, riverY + 16f, width, riverY + 12f)
            }
            drawPath(
                path = wavePath,
                color = Color.White.copy(alpha = 0.6f)
            )

            // 5. Right riverside with traditional village cottage, tin roof & green trees
            val bankStartX = width * 0.55f
            val bankPath = Path().apply {
                moveTo(bankStartX, height)
                quadraticTo(width * 0.65f, height * 0.68f, width, height * 0.65f)
                lineTo(width, height)
                close()
            }
            drawPath(
                path = bankPath,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF81C784).copy(alpha = 0.75f),
                        Color(0xFF66BB6A).copy(alpha = 0.85f),
                        Color(0xFF4CAF50).copy(alpha = 0.9f)
                    ),
                    startX = bankStartX,
                    endX = width
                )
            )

            // Village Cottage with red & tin roof
            val cottageX = width * 0.70f
            val cottageY = height * 0.60f
            // Cottage Walls
            drawRect(
                color = Color(0xFFEFEBE9),
                topLeft = Offset(cottageX, cottageY),
                size = Size(width * 0.16f, height * 0.15f)
            )
            // Cottage Roof
            val roofPath = Path().apply {
                moveTo(cottageX - 10f, cottageY)
                lineTo(cottageX + (width * 0.08f), cottageY - 18f)
                lineTo(cottageX + (width * 0.17f), cottageY)
                close()
            }
            drawPath(path = roofPath, color = Color(0xFFB0BEC5)) // Corrugated tin roof

            // Prokah branded mini shop stall next to house
            val stallX = width * 0.86f
            val stallY = height * 0.62f
            drawRect(
                color = Color.White,
                topLeft = Offset(stallX, stallY),
                size = Size(width * 0.12f, height * 0.13f)
            )
            // Orange brand banner on stall
            drawRect(
                color = Color(0xFFE52E2D),
                topLeft = Offset(stallX, stallY),
                size = Size(width * 0.12f, 6f)
            )

            // Palm tree behind cottage
            val palmX = width * 0.83f
            val palmY = height * 0.44f
            // Trunk
            val trunkPath = Path().apply {
                moveTo(palmX, height * 0.66f)
                quadraticTo(palmX + 4f, height * 0.54f, palmX + 2f, palmY)
                lineTo(palmX + 5f, palmY)
                quadraticTo(palmX + 7f, height * 0.54f, palmX + 4f, height * 0.66f)
                close()
            }
            drawPath(path = trunkPath, color = Color(0xFF6D4C41))
            // Palm fronds
            val frondColor = Color(0xFF2E7D32)
            drawCircle(frondColor, radius = 16f, center = Offset(palmX - 8f, palmY - 4f))
            drawCircle(frondColor, radius = 18f, center = Offset(palmX + 4f, palmY - 8f))
            drawCircle(frondColor, radius = 16f, center = Offset(palmX + 12f, palmY - 2f))
        }
    }
}
