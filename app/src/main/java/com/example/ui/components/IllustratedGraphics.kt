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

// ============================================================
// SCI BASE COLLECTIONS - SHARED ILLUSTRATION COLOURS
// ============================================================

// SCI brand colours
private val SciRed = Color(0xFFFF0000)
private val SciDarkRed = Color(0xFFCC0000)
private val SciNavy = Color(0xFF0F268C)
private val SciDarkNavy = Color(0xFF08175E)
private val SciGold = Color(0xFFE69A00)
private val SciLightBackground = Color(0xFFF5F6FA)

// Compatibility colours.
// Retained because other project files may reference these names.
val KenyanGreen = Color(0xFF0B6638)
val KenyanDarkGreen = Color(0xFF074826)
val KenyanLightGreen = Color(0xFFE8F5E9)
val KenyanRed = Color(0xFFBA0C2F)
val KenyanGold = Color(0xFFF59E0B)
val LightSurfaceBg = Color(0xFFF9FAFB)
val CardBorderGray = Color(0xFFE5E7EB)

// ============================================================
// KENYAN FLAG RIBBON
// Preserve the official Kenyan flag colours.
// ============================================================

@Composable
fun KenyanFlagRibbon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val black = Color(0xFF000000)
        val white = Color(0xFFFFFFFF)
        val red = Color(0xFFBB0000)
        val green = Color(0xFF006600)

        val flagPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            cubicTo(w * 0.8f, h * 0.5f, w * 0.4f, h * 0.8f, 0f, h)
            close()
        }

        drawPath(flagPath, black)

        val whiteUpper = Path().apply {
            moveTo(0f, h * 0.24f)
            cubicTo(w * 0.3f, h * 0.27f, w * 0.7f, h * 0.18f, w, h * 0.12f)
            lineTo(w, h * 0.22f)
            cubicTo(w * 0.6f, h * 0.35f, w * 0.3f, h * 0.37f, 0f, h * 0.35f)
            close()
        }

        drawPath(whiteUpper, white)

        val redPath = Path().apply {
            moveTo(0f, h * 0.35f)
            cubicTo(w * 0.3f, h * 0.37f, w * 0.7f, h * 0.28f, w, h * 0.22f)
            lineTo(w, h * 0.62f)
            cubicTo(w * 0.6f, h * 0.75f, w * 0.3f, h * 0.77f, 0f, h * 0.76f)
            close()
        }

        drawPath(redPath, red)

        val whiteLower = Path().apply {
            moveTo(0f, h * 0.76f)
            cubicTo(w * 0.3f, h * 0.77f, w * 0.7f, h * 0.68f, w, h * 0.62f)
            lineTo(w, h * 0.72f)
            cubicTo(w * 0.6f, h * 0.85f, w * 0.3f, h * 0.88f, 0f, h * 0.87f)
            close()
        }

        drawPath(whiteLower, white)

        val greenPath = Path().apply {
            moveTo(0f, h * 0.87f)
            cubicTo(w * 0.3f, h * 0.88f, w * 0.6f, h * 0.82f, w, h * 0.72f)
            lineTo(w, h * 0.9f)
            cubicTo(w * 0.5f, h * 0.98f, w * 0.2f, h, 0f, h)
            close()
        }

        drawPath(greenPath, green)
    }
}

// ============================================================
// BIASHARAHUB LOGO
// ============================================================

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
                Brush.linearGradient(
                    colors = listOf(
                        SciRed,
                        SciDarkRed,
                        SciNavy,
                        SciDarkNavy
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size((sizeDp * 0.65f).dp)
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2.4f

            // Lightbulb outline
            drawCircle(
                color = Color.White,
                radius = radius * 0.75f,
                center = Offset(
                    center.x,
                    center.y - radius * 0.15f
                ),
                style = Stroke(width = 5f)
            )

            // Left gold leaf
            val leftLeaf = Path().apply {
                moveTo(center.x, center.y + radius * 0.25f)
                cubicTo(
                    center.x - radius * 0.4f,
                    center.y,
                    center.x - radius * 0.5f,
                    center.y - radius * 0.4f,
                    center.x - radius * 0.1f,
                    center.y - radius * 0.5f
                )
                cubicTo(
                    center.x + radius * 0.1f,
                    center.y - radius * 0.3f,
                    center.x,
                    center.y,
                    center.x,
                    center.y + radius * 0.25f
                )
                close()
            }

            drawPath(
                path = leftLeaf,
                color = SciGold,
                style = Fill
            )

            // Right red-highlighted leaf
            val rightLeaf = Path().apply {
                moveTo(center.x, center.y + radius * 0.25f)
                cubicTo(
                    center.x + radius * 0.4f,
                    center.y,
                    center.x + radius * 0.5f,
                    center.y - radius * 0.35f,
                    center.x + radius * 0.15f,
                    center.y - radius * 0.45f
                )
                cubicTo(
                    center.x - radius * 0.05f,
                    center.y - radius * 0.25f,
                    center.x,
                    center.y,
                    center.x,
                    center.y + radius * 0.25f
                )
                close()
            }

            drawPath(
                path = rightLeaf,
                color = Color.White,
                style = Fill
            )
        }
    }
}

// ============================================================
// NAIROBI SKYLINE
// ============================================================

@Composable
fun NairobiSkylineIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Navy dusk background
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF152B70),
                    Color(0xFF0F268C),
                    Color(0xFF08175E)
                )
            )
        )

        // Subtle gold sunset
        drawCircle(
            color = SciGold.copy(alpha = 0.8f),
            radius = h * 0.09f,
            center = Offset(w * 0.78f, h * 0.28f)
        )

        // Distant city buildings
        val buildingColor = Color(0xFF172B62)

        drawRect(
            color = buildingColor,
            topLeft = Offset(w * 0.08f, h * 0.48f),
            size = Size(w * 0.12f, h * 0.52f)
        )

        drawRect(
            color = buildingColor.copy(alpha = 0.9f),
            topLeft = Offset(w * 0.22f, h * 0.38f),
            size = Size(w * 0.1f, h * 0.62f)
        )

        drawRect(
            color = buildingColor,
            topLeft = Offset(w * 0.35f, h * 0.42f),
            size = Size(w * 0.09f, h * 0.58f)
        )

        // KICC tower
        val kiccLeft = w * 0.48f
        val kiccWidth = w * 0.08f
        val kiccTop = h * 0.25f

        drawRect(
            color = Color(0xFF263D83),
            topLeft = Offset(kiccLeft, kiccTop),
            size = Size(kiccWidth, h - kiccTop)
        )

        drawCircle(
            color = Color(0xFF263D83),
            radius = kiccWidth * 0.75f,
            center = Offset(
                kiccLeft + kiccWidth / 2f,
                kiccTop
            )
        )

        drawLine(
            color = SciGold,
            start = Offset(
                kiccLeft + kiccWidth / 2f,
                kiccTop - kiccWidth * 0.75f
            ),
            end = Offset(
                kiccLeft + kiccWidth / 2f,
                kiccTop - kiccWidth * 1.5f
            ),
            strokeWidth = 4f
        )

        // Additional high-rise buildings
        drawRect(
            color = Color(0xFF1B3474),
            topLeft = Offset(w * 0.60f, h * 0.42f),
            size = Size(w * 0.08f, h * 0.58f)
        )

        drawRect(
            color = Color(0xFF243D7A),
            topLeft = Offset(w * 0.72f, h * 0.5f),
            size = Size(w * 0.11f, h * 0.5f)
        )

        drawRect(
            color = Color(0xFF192D65),
            topLeft = Offset(w * 0.86f, h * 0.44f),
            size = Size(w * 0.08f, h * 0.56f)
        )

        // Building window lights
        for (row in 0..3) {
            for (column in 0..2) {
                drawCircle(
                    color = SciGold.copy(alpha = 0.8f),
                    radius = 2.5f,
                    center = Offset(
                        w * (0.24f + column * 0.025f),
                        h * (0.48f + row * 0.09f)
                    )
                )
            }
        }

        // Ground hill
        val hillPath = Path().apply {
            moveTo(0f, h)
            cubicTo(
                w * 0.25f,
                h * 0.75f,
                w * 0.75f,
                h * 0.8f,
                w,
                h * 0.7f
            )
            lineTo(w, h)
            close()
        }

        drawPath(
            path = hillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF102C4D),
                    Color(0xFF081A35)
                )
            )
        )

        // Small environmental greenery
        for (i in 0..12) {
            val cx = w * (i / 12f)
            val cy = h * 0.87f + (i % 3) * 6f

            drawCircle(
                color = KenyanGreen.copy(alpha = 0.9f),
                radius = 16f + (i % 4) * 4f,
                center = Offset(cx, cy)
            )
        }
    }
}

// ============================================================
// ONBOARDING AVATAR ILLUSTRATIONS
// ============================================================

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

        // Brand-coloured backdrop
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFE5E5),
                            Color(0xFFE8ECFF),
                            Color(0xFFFFFFFF)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            when (step) {

                // STEP 1: DISCOVER
                1 -> {
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

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SciRed,
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneAndroid,
                                    contentDescription = "Smartphone",
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

                // STEP 2: PLAN
                2 -> {
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
                                contentDescription = "Entrepreneur planning",
                                tint = Color(0xFFFFCC80),
                                modifier = Modifier.size(70.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SciNavy,
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = "Growth",
                                    tint = SciGold,
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

                // STEP 3: LAUNCH
                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(SciNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AssignmentTurnedIn,
                                contentDescription = "Business guidance",
                                tint = SciGold,
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SciRed,
                            shadowElevation = 4.dp
                        ) {
                            Text(
                                text = "4-Step Launch Blueprint",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Floating supporting icons
        when (step) {
            1 -> {
                FloatingPillIcon(
                    Icons.Default.Lightbulb,
                    SciGold,
                    Alignment.TopStart,
                    20.dp,
                    20.dp
                )

                FloatingPillIcon(
                    Icons.Default.School,
                    SciNavy,
                    Alignment.TopEnd,
                    20.dp,
                    20.dp
                )

                FloatingPillIcon(
                    Icons.Default.Settings,
                    Color(0xFF687386),
                    Alignment.BottomEnd,
                    24.dp,
                    20.dp
                )

                FloatingPillIcon(
                    Icons.Default.BarChart,
                    SciRed,
                    Alignment.BottomStart,
                    24.dp,
                    20.dp
                )
            }

            2 -> {
                FloatingPillIcon(
                    Icons.Default.AccountBalanceWallet,
                    SciNavy,
                    Alignment.TopStart,
                    20.dp,
                    20.dp
                )

                FloatingPillIcon(
                    Icons.Default.AttachMoney,
                    SciGold,
                    Alignment.TopEnd,
                    20.dp,
                    20.dp
                )

                FloatingPillIcon(
                    Icons.Default.Insights,
                    SciRed,
                    Alignment.BottomEnd,
                    24.dp,
                    20.dp
                )
            }

            3 -> {
                FloatingPillBadge(
                    "✓ Requirements",
                    Alignment.TopEnd,
                    10.dp,
                    10.dp
                )

                FloatingPillBadge(
                    "✓ Customers",
                    Alignment.CenterEnd,
                    0.dp,
                    4.dp
                )

                FloatingPillBadge(
                    "✓ Marketing",
                    Alignment.BottomEnd,
                    30.dp,
                    10.dp
                )

                FloatingPillBadge(
                    "✓ Next Steps",
                    Alignment.BottomStart,
                    30.dp,
                    10.dp
                )
            }
        }
    }
}

// ============================================================
// FLOATING ICON COMPONENT
// ============================================================

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

// ============================================================
// FLOATING CHECKLIST BADGE
// ============================================================

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
            .padding(
                top = topOffset,
                end = endOffset,
                bottom = topOffset,
                start = endOffset
            ),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF16834A),
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = label.removePrefix("✓ ").trim(),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF172033)
            )
        }
    }
}

// ============================================================
// BUSINESS IDEA CATEGORY THUMBNAILS
// ============================================================

@Composable
fun IdeaCardThumbnail(
    type: String,
    modifier: Modifier = Modifier
) {
    val normalizedType = type.trim().lowercase()

    val bgBrush = when (normalizedType) {
        "farming", "agriculture", "agribusiness" ->
            Brush.verticalGradient(
                listOf(
                    Color(0xFF2E7D32),
                    Color(0xFF1B5E20)
                )
            )

        "electronics", "phone repair" ->
            Brush.verticalGradient(
                listOf(
                    Color(0xFF1E293B),
                    Color(0xFF0F172A)
                )
            )

        "tech", "technology", "digital" ->
            Brush.verticalGradient(
                listOf(
                    SciNavy,
                    SciDarkNavy
                )
            )

        "solar", "renewable energy" ->
            Brush.verticalGradient(
                listOf(
                    Color(0xFF0284C7),
                    Color(0xFF0369A1)
                )
            )

        "food", "food business" ->
            Brush.verticalGradient(
                listOf(
                    Color(0xFFEA580C),
                    Color(0xFFC2410C)
                )
            )

        "bakery", "baking" ->
            Brush.verticalGradient(
                listOf(
                    Color(0xFFD97706),
                    Color(0xFFB45309)
                )
            )

        "juice", "beverages" ->
            Brush.verticalGradient(
                listOf(
                    Color(0xFF10B981),
                    Color(0xFF059669)
                )
            )

        "catering", "hospitality" ->
            Brush.verticalGradient(
                listOf(
                    Color(0xFF9333EA),
                    Color(0xFF7E22CE)
                )
            )

        "fashion", "beauty" ->
            Brush.verticalGradient(
                listOf(
                    Color(0xFFBE185D),
                    Color(0xFF831843)
                )
            )

        "transport", "delivery" ->
            Brush.verticalGradient(
                listOf(
                    Color(0xFF334155),
                    Color(0xFF0F172A)
                )
            )

        else ->
            Brush.verticalGradient(
                listOf(
                    SciRed,
                    SciNavy
                )
            )
    }

    val icon = when (normalizedType) {
        "farming", "agriculture", "agribusiness" ->
            Icons.Default.Eco

        "electronics", "phone repair" ->
            Icons.Default.Smartphone

        "tech", "technology", "digital" ->
            Icons.Default.Laptop

        "solar", "renewable energy" ->
            Icons.Default.WbSunny

        "food", "food business" ->
            Icons.Default.Fastfood

        "bakery", "baking" ->
            Icons.Default.Cake

        "juice", "beverages" ->
            Icons.Default.LocalDrink

        "catering", "hospitality" ->
            Icons.Default.Restaurant

        "fashion", "beauty" ->
            Icons.Default.Checkroom

        "transport", "delivery" ->
            Icons.Default.DirectionsBike

        else ->
            Icons.Default.Storefront
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
            tint = Color.White.copy(alpha = 0.95f),
            modifier = Modifier.size(36.dp)
        )
    }
}