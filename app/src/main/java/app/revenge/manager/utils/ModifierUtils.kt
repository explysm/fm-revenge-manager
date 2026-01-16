package app.revenge.manager.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.revenge.manager.domain.manager.PreferenceManager
import org.koin.androidx.compose.get

fun Modifier.glow(
    color: Color,
    radius: Dp = 20.dp,
    alpha: Float = 0.5f
) = this.drawBehind {
    val transparentColor = color.copy(alpha = 0f).toArgb()
    val shadowColor = color.copy(alpha = alpha).toArgb()
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            radius.toPx(),
            0f,
            0f,
            shadowColor
        )
        it.drawCircle(
            center,
            size.minDimension / 2f,
            paint
        )
    }
}

fun Modifier.contentDescription(description: String, merge: Boolean = false): Modifier = semantics(mergeDescendants = merge) {
    contentDescription = description
}

fun Modifier.contentDescription(res: Int, vararg param: Any, merge: Boolean = false): Modifier
    = composed { contentDescription(stringResource(res, *param), merge) }

inline fun Modifier.thenIf(predicate: Boolean, block: Modifier.() -> Modifier): Modifier =
    if (predicate) then(Modifier.Companion.block()) else this

fun Modifier.frosted(
    alpha: Float = 0.3f
) = composed {
    val prefs: PreferenceManager = get()
    if (prefs.frostedGlass) {
        this.drawBehind {
            drawRect(Color.White.copy(alpha = alpha))
        }
    } else {
        this
    }
}