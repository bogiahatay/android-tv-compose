package net.habui.tv.core.focus

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.BringIntoViewSpec
import androidx.compose.foundation.gestures.LocalBringIntoViewSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

/**
 * Supports:
 *
 * 1. Fraction-based positioning
 * 2. Fixed X/Y offset positioning
 *
 * Examples:
 *
 * Fraction mode:
 * parentFraction = 0.3f
 * childFraction = 0f
 *
 * Fixed offset mode:
 * targetOffsetXPx = 48.dp.toPx()
 * targetOffsetYPx = 32.dp.toPx()
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PositionFocusedItemInLazyLayout(
    parentFraction: Float = 0.3f,
    childFraction: Float = 0f,
    targetOffsetXPx: Float? = null,
    targetOffsetYPx: Float? = null,
    content: @Composable () -> Unit,
) {

    val bringIntoViewSpec = remember(
        parentFraction,
        childFraction,
        targetOffsetXPx,
        targetOffsetYPx
    ) {

        object : BringIntoViewSpec {

            override fun calculateScrollDistance(
                offset: Float,
                size: Float,
                containerSize: Float
            ): Float {

                /**
                 * Detect scroll direction
                 *
                 * Horizontal LazyRow:
                 * containerSize usually represents width
                 *
                 * Vertical LazyColumn:
                 * containerSize usually represents height
                 *
                 * Compose currently does not expose direction directly,
                 * so we infer based on which offset is provided.
                 */

                val fixedOffset = when {
                    targetOffsetXPx != null -> targetOffsetXPx
                    targetOffsetYPx != null -> targetOffsetYPx
                    else -> null
                }

                /**
                 * Fixed offset mode
                 */
                if (fixedOffset != null) {
                    return offset - fixedOffset
                }

                /**
                 * Fraction-based mode
                 */
                val initialTargetForLeadingEdge =
                    parentFraction * containerSize -
                            (childFraction * size)

                val targetForLeadingEdge =
                    if (
                        size <= containerSize &&
                        (containerSize - initialTargetForLeadingEdge) < size
                    ) {
                        containerSize - size
                    } else {
                        initialTargetForLeadingEdge
                    }

                return offset - targetForLeadingEdge
            }
        }
    }

    CompositionLocalProvider(
        LocalBringIntoViewSpec provides bringIntoViewSpec,
        content = content,
    )
}