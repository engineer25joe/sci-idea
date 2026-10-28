package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Brand Color Palette matching mockup image_84396db9.png
val KenyanGreen = Color(0xFF0B6638)
val KenyanDarkGreen = Color(0xFF074826)
val KenyanLightGreen = Color(0xFFE8F5E9)
val KenyanRed = Color(0xFFBA0C2F)
val KenyanGold = Color(0xFFF59E0B)
val LightSurfaceBg = Color(0xFFF9FAFB)
val CardBorderGray = Color(0xFFE5E7EB)

@Composable
fun KenyanFlagRibbon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Curved arch ribbon banner
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            cubicTo(w * 0.8f, h * 0.5f, w * 0.4f, h * 0.8f, 0f, h)
            close()
        }

        // Draw Kenya Stripes: Black, White, Red, White, Green
        drawPath(
            path = path,
            color = Color(0xFF1E1E1E)
        )
        // Red Stripe
        val redPath = Path().apply {
            moveTo(0f, h * 0.3f)
            cubicTo(w * 0.3f, h * 0.35f, w * 0.7f, h * 0.25f, w, h * 0.15f)
            lineTo(w, h * 0.5f)
            cubicTo(w * 0.6f, h * 0.65f, w * 0.3f, h * 0.65f, 0f, h * 0.7f)
            close()
        }
        drawPath(path = redPath, color = KenyanRed)

        // Green Stripe
        val greenPath = Path().apply {
            moveTo(0f, h * 0.75f)
            cubicTo(w * 0.3f, h * 0.7f, w * 0.6f, h * 0.6f, w, h * 0.55f)
            lineTo(w, h * 0.85f)
            cubicTo(w * 0.5f, h * 0.95f, w * 0.2f, h * 0.98f, 0f, h)
            close()
        }
        drawPath(path = greenPath, color = KenyanGreen)
    }
}

@Composable
fun BiasharaLogoBadge(
    modifier: Modifier = Modifier,
    sizeDp: Int = 80
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF22C55E), KenyanGreen)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size((sizeDp * 0.65f).dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2.4f

            // Lightbulb outline in white
            drawCircle(
                color = Color.White,
                radius = radius * 0.75f,
                center = Offset(center.x, center.y - radius * 0.15f),
                style = Stroke(width = 5f)
            )

            // Sprout leaves inside bulb in bright gold/amber
            val leafPath = Path().apply {
                moveTo(center.x, center.y + radius * 0.25f)
                cubicTo(center.x - radius * 0.4f, center.y, center.x - radius * 0.5f, center.y - radius * 0.4f, center.x - radius * 0.1f, center.y - radius * 0.5f)
                cubicTo(center.x + radius * 0.1f, center.y - radius * 0.3f, center.x, center.y, center.x, center.y + radius * 0.25f)
                close()
            }
            drawPath(path = leafPath, color = KenyanGold, style = Fill)

            val leaf2Path = Path().apply {
                moveTo(center.x, center.y + radius * 0.25f)
                cubicTo(center.x + radius * 0.4f, center.y, center.x + radius * 0.5f, center.y - radius * 0.35f, center.x + radius * 0.15f, center.y - radius * 0.45f)
                cubicTo(center.x - radius * 0.05f, center.y - radius * 0.25f, center.x, center.y, center.x, center.y + radius * 0.25f)
                close()
            }
            drawPath(path = leaf2Path, color = Color.White, style = Fill)
        }
    }
}

@Composable
fun NairobiSkylineIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Ground hill gradient
        val hillPath = Path().apply {
            moveTo(0f, h)
            cubicTo(w * 0.25f, h * 0.75f, w * 0.75f, h * 0.8f, w, h * 0.7f)
            lineTo(w, h)
            close()
        }
        drawPath(
            path = hillPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF1E3A2B), Color(0xFF0F1E16))
            )
        )

        // Skyline silhouettes (KICC tower, Times Tower, city buildings)
        val buildingColor = Color(0x992B4A3C)

        // KICC Cylinder tower
        val kiccLeft = w * 0.48f
        val kiccWidth = w * 0.08f
        val kiccTop = h * 0.25f
        drawRect(
            color = buildingColor,
            topLeft = Offset(kiccLeft, kiccTop),
            size = Size(kiccWidth, h - kiccTop)
        )
        // KICC top saucer & spire
        drawCircle(
            color = buildingColor,
            radius = kiccWidth * 0.75f,
            center = Offset(kiccLeft + kiccWidth / 2f, kiccTop)
        )
        drawLine(
            color = buildingColor,
            start = Offset(kiccLeft + kiccWidth / 2f, kiccTop - kiccWidth * 0.75f),
            end = Offset(kiccLeft + kiccWidth / 2f, kiccTop - kiccWidth * 1.5f),
            strokeWidth = 4f
        )

        // Neighboring high-rise towers
        drawRect(
            color = Color(0x662B4A3C),
            topLeft = Offset(w * 0.35f, h * 0.4f),
            size = Size(w * 0.09f, h * 0.6f)
        )
        drawRect(
            color = Color(0x882B4A3C),
            topLeft = Offset(w * 0.22f, h * 0.48f),
            size = Size(w * 0.1f, h * 0.52f)
        )
        drawRect(
            color = Color(0x772B4A3C),
            topLeft = Offset(w * 0.60f, h * 0.42f),
            size = Size(w * 0.08f, h * 0.58f)
        )
        drawRect(
            color = Color(0x992B4A3C),
            topLeft = Offset(w * 0.72f, h * 0.5f),
            size = Size(w * 0.11f, h * 0.5f)
        )

        // Green trees at the base
        for (i in 0..12) {
            val cx = w * (i / 12f)
            val cy = h * 0.85f + (i % 3) * 6f
            drawCircle(
                color = Color(0xAA107C41),
                radius = 16f + (i % 4) * 4f,
                center = Offset(cx, cy)
            )
        }
    }
}

@Composable
fun OnboardingAvatarIllustration(
    step: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {
        // Soft mint circular backdrop
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            when (step) {
                1 -> {
                    // Female Kenyan entrepreneur with smartphone and floating idea bubbles
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF8D5B4C)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "Entrepreneur",
                                tint = Color(0xFFFDE0D9),
                                modifier = Modifier.size(70.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Orange top / smartphone
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = KenyanGreen,
                                shadowElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneAndroid,
                                        contentDescription = "Smart Phone",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Discover",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Male Kenyan entrepreneur with laptop and green growth trend
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF5D4037)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Entrepreneur Laptop",
                                tint = Color(0xFFFFCC80),
                                modifier = Modifier.size(70.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF59E0B),
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = "Growth",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Budget: KSh 10K+",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                else -> {
                    // Female entrepreneur with checklist guidance
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6D4C41)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AssignmentTurnedIn,
                                contentDescription = "Guidance",
                                tint = Color(0xFFC8E6C9),
                                modifier = Modifier.size(60.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = KenyanGreen,
                            shadowElevation = 4.dp
                        ) {
                            Text(
                                text = "4-Step Launch Blueprint",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Floating idea bubbles around the circle
        when (step) {
            1 -> {
                FloatingPillIcon(Icons.Default.Lightbulb, KenyanGold, Alignment.TopStart, 20.dp, 20.dp)
                FloatingPillIcon(Icons.Default.School, Color(0xFF3B82F6), Alignment.TopEnd, 20.dp, 20.dp)
                FloatingPillIcon(Icons.Default.Settings, Color(0xFF6B7280), Alignment.BottomEnd, 24.dp, 20.dp)
                FloatingPillIcon(Icons.Default.BarChart, KenyanGreen, Alignment.BottomStart, 24.dp, 20.dp)
            }
            2 -> {
                FloatingPillIcon(Icons.Default.AccountBalanceWallet, KenyanGreen, Alignment.TopStart, 20.dp, 20.dp)
                FloatingPillIcon(Icons.Default.AttachMoney, KenyanGold, Alignment.TopEnd, 20.dp, 20.dp)
                FloatingPillIcon(Icons.Default.Insights, Color(0xFF10B981), Alignment.BottomEnd, 24.dp, 20.dp)
            }
            3 -> {
                FloatingPillBadge("✓ Requirements", Alignment.TopEnd, 10.dp, 10.dp)
                FloatingPillBadge("✓ Customers", Alignment.CenterEnd, 0.dp, 4.dp)
                FloatingPillBadge("✓ Marketing", Alignment.BottomEnd, 30.dp, 10.dp)
                FloatingPillBadge("✓ Next Steps", Alignment.BottomStart, 30.dp, 10.dp)
            }
        }
    }
}

@Composable
private fun BoxScope.FloatingPillIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    alignment: Alignment,
    topOrBottom: androidx.compose.ui.unit.Dp,
    leftOrRight: androidx.compose.ui.unit.Dp
) {
    Surface(
        modifier = Modifier
            .align(alignment)
            .padding(all = 12.dp),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun BoxScope.FloatingPillBadge(
    label: String,
    alignment: Alignment,
    topOffset: androidx.compose.ui.unit.Dp,
    endOffset: androidx.compose.ui.unit.Dp
) {
    Surface(
        modifier = Modifier
            .align(alignment)
            .padding(top = topOffset, end = endOffset, bottom = topOffset, start = endOffset),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = KenyanGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label.removePrefix("✓ ").trim(),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )
        }
    }
}

@Composable
fun IdeaCardThumbnail(
    type: String,
    modifier: Modifier = Modifier
) {
    val bgBrush = when (type) {
        "farming" -> Brush.verticalGradient(listOf(Color(0xFF2E7D32), Color(0xFF1B5E20)))
        "electronics" -> Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
        "tech" -> Brush.verticalGradient(listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)))
        "solar" -> Brush.verticalGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1)))
        "food" -> Brush.verticalGradient(listOf(Color(0xFFEA580C), Color(0xFFC2410C)))
        "bakery" -> Brush.verticalGradient(listOf(Color(0xFFD97706), Color(0xFFB45309)))
        "juice" -> Brush.verticalGradient(listOf(Color(0xFF10B981), Color(0xFF059669)))
        "catering" -> Brush.verticalGradient(listOf(Color(0xFF9333EA), Color(0xFF7E22CE)))
        else -> Brush.verticalGradient(listOf(KenyanGreen, KenyanDarkGreen))
    }

    val icon = when (type) {
        "farming" -> Icons.Default.Eco
        "electronics" -> Icons.Default.Smartphone
        "tech" -> Icons.Default.Laptop
        "solar" -> Icons.Default.WbSunny
        "food" -> Icons.Default.Fastfood
        "bakery" -> Icons.Default.Cake
        "juice" -> Icons.Default.LocalDrink
        "catering" -> Icons.Default.Restaurant
        else -> Icons.Default.Storefront
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgBrush),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(36.dp)
        )
    }
}
