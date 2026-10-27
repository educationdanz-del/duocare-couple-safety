package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MapRadarCanvas(
    partnerName: String,
    partnerSpeedKmh: Float,
    distanceKm: Double,
    isPartnerSharing: Boolean,
    isGhostMode: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarTransition")

    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 68f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Pulse"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseAlpha"
    )

    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Sweep"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(Color(0xFF0F172A), RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(24.dp))
            .padding(4.dp)
            .testTag("radar_map_canvas")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Center of radar (User's location)
            val userCenter = Offset(w * 0.30f, h * 0.65f)

            // Partner location (offset based on distance representation)
            val partnerCenter = Offset(w * 0.72f, h * 0.35f)

            // 1. Draw subtle grid
            val gridSpacing = 40.dp.toPx()
            var x = 0f
            while (x < w) {
                drawLine(
                    color = Color(0xFF1E293B).copy(alpha = 0.5f),
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 1f
                )
                x += gridSpacing
            }
            var y = 0f
            while (y < h) {
                drawLine(
                    color = Color(0xFF1E293B).copy(alpha = 0.5f),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1f
                )
                y += gridSpacing
            }

            // 2. Safe zone circles (e.g. Home & Office zones)
            drawCircle(
                color = Color(0xFF10B981).copy(alpha = 0.12f),
                radius = 75f,
                center = Offset(w * 0.22f, h * 0.30f)
            )
            drawCircle(
                color = Color(0xFF10B981).copy(alpha = 0.4f),
                radius = 75f,
                center = Offset(w * 0.22f, h * 0.30f),
                style = Stroke(
                    width = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                )
            )

            // 3. Concentric radar rings around user
            val rings = listOf(50f, 110f, 180f, 250f)
            rings.forEach { r ->
                drawCircle(
                    color = Color(0xFF334155).copy(alpha = 0.45f),
                    radius = r,
                    center = userCenter,
                    style = Stroke(width = 1.5f)
                )
            }

            // 4. Subtle sweep beam
            val sweepRad = Math.toRadians(sweepAngle.toDouble())
            val beamEnd = Offset(
                userCenter.x + 220f * cos(sweepRad).toFloat(),
                userCenter.y + 220f * sin(sweepRad).toFloat()
            )
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE11D48).copy(alpha = 0.4f),
                        Color(0xFFE11D48).copy(alpha = 0.0f)
                    ),
                    start = userCenter,
                    end = beamEnd
                ),
                start = userCenter,
                end = beamEnd,
                strokeWidth = 2.5f
            )

            // 5. Connecting line between You & Partner
            if (isPartnerSharing && !isGhostMode) {
                drawLine(
                    color = Color(0xFFFB7185).copy(alpha = 0.6f),
                    start = userCenter,
                    end = partnerCenter,
                    strokeWidth = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), pulseRadius)
                )
            }

            // 6. Draw User Node (Blue / Indigo)
            drawCircle(
                color = Color(0xFF6366F1).copy(alpha = 0.3f),
                radius = 26f,
                center = userCenter
            )
            drawCircle(
                color = Color(0xFF4F46E5),
                radius = 14f,
                center = userCenter
            )
            drawCircle(
                color = Color.White,
                radius = 5f,
                center = userCenter
            )

            // 7. Draw Partner Node (Rose / Pink with animated pulse)
            if (isPartnerSharing && !isGhostMode) {
                // Expanding pulse halo
                drawCircle(
                    color = Color(0xFFE11D48).copy(alpha = pulseAlpha),
                    radius = pulseRadius,
                    center = partnerCenter
                )
                // Solid inner dot
                drawCircle(
                    color = Color(0xFFE11D48),
                    radius = 16f,
                    center = partnerCenter
                )
                drawCircle(
                    color = Color.White,
                    radius = 6f,
                    center = partnerCenter
                )
            } else {
                // Ghost or paused state
                drawCircle(
                    color = Color(0xFF64748B).copy(alpha = 0.5f),
                    radius = 16f,
                    center = partnerCenter
                )
            }

            // 8. Native canvas text for labels & badges
            val paintText = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 30f
                isAntiAlias = true
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            val paintSub = android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#94A3B8")
                textSize = 24f
                isAntiAlias = true
            }
            val paintBadgeBg = android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#E11D48")
                isAntiAlias = true
            }

            // Label for "Anda"
            drawContext.canvas.nativeCanvas.drawText("Anda (Di Sini)", userCenter.x - 60f, userCenter.y + 40f, paintSub)

            // Label for Partner
            if (isPartnerSharing && !isGhostMode) {
                drawContext.canvas.nativeCanvas.drawText(partnerName, partnerCenter.x - 50f, partnerCenter.y - 30f, paintText)
                val statusText = if (partnerSpeedKmh > 5f) "${partnerSpeedKmh.toInt()} km/j • Bergerak" else "Berdiam"
                drawContext.canvas.nativeCanvas.drawText(statusText, partnerCenter.x - 50f, partnerCenter.y - 8f, paintSub)

                // Distance badge in between
                val midX = (userCenter.x + partnerCenter.x) / 2
                val midY = (userCenter.y + partnerCenter.y) / 2
                val badgeText = "$distanceKm km"
                val textWidth = paintText.measureText(badgeText)
                val rectF = android.graphics.RectF(midX - textWidth / 2 - 16f, midY - 26f, midX + textWidth / 2 + 16f, midY + 12f)
                drawContext.canvas.nativeCanvas.drawRoundRect(rectF, 12f, 12f, paintBadgeBg)
                drawContext.canvas.nativeCanvas.drawText(badgeText, midX - textWidth / 2, midY - 2f, paintText)
            } else {
                val pausedText = if (isGhostMode) "Mode Privasi Aktif" else "Berbagi Dijeda"
                drawContext.canvas.nativeCanvas.drawText(partnerName, partnerCenter.x - 50f, partnerCenter.y - 25f, paintText)
                drawContext.canvas.nativeCanvas.drawText(pausedText, partnerCenter.x - 50f, partnerCenter.y - 5f, paintSub)
            }

            // Safe Zone Tag
            drawContext.canvas.nativeCanvas.drawText("🛡️ Zona Aman: Rumah", w * 0.22f - 90f, h * 0.30f, paintSub)
        }
    }
}
