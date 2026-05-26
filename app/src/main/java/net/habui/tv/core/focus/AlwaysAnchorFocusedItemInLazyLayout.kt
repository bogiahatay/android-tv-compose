package net.habui.tv.core.focus

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.BringIntoViewSpec
import androidx.compose.foundation.gestures.LocalBringIntoViewSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlwaysAnchorFocusedItemInLazyLayout(
    targetOffsetPx: Float,
    content: @Composable () -> Unit,
) {

    val bringIntoViewSpec = remember(targetOffsetPx) {

        object : BringIntoViewSpec {

            override fun calculateScrollDistance(
                offset: Float,
                size: Float,
                containerSize: Float
            ): Float {

                /**
                 * Always keep the focused item aligned
                 * to the same anchor position,
                 * even near the end of the list.
                 */
                return offset - targetOffsetPx
            }
        }
    }

    CompositionLocalProvider(
        LocalBringIntoViewSpec provides bringIntoViewSpec,
        content = content
    )
}