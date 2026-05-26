package net.habui.tv.core.designsystem

import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ColorScheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme

val TvPrimaryBlue = Color(0xFF4F8DFF)
val TvPrimaryGreen = Color(0xFF35C46F)
val TvPrimaryRed = Color(0xFFFF5A5F)
val TvPrimaryShamrock = Color(0xFF2ECC71)
val TvPrimaryAegean = Color(0xFF3D8BFF)
val TvPrimaryRosewood = Color(0xFFC85A7A)
val TvPrimaryDijon = Color(0xFFD9A441)

val ColorScheme.tvFocusBorder: Color
    get() = primary

val ColorScheme.tvImagePlaceholder: Color
    get() = surfaceVariant

val ColorScheme.tvImageError: Color
    get() = borderVariant

val ColorScheme.tvPlayerBackground: Color
    get() = Color.Black

val ColorScheme.tvPlayerOverlayScrim: Color
    get() = Color.Black

val ColorScheme.tvOnPlayerOverlay: Color
    get() = Color.White

val ColorScheme.tvLiveContainer: Color
    get() = error

val ColorScheme.tvOnLiveContainer: Color
    get() = onError

fun tvColorScheme(
    darkTheme: Boolean,
    primaryColor: Color
): ColorScheme {
    return if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = Color.Black,
            primaryContainer = Color(0xFF243A63),
            onPrimaryContainer = Color.White,
            inversePrimary = primaryColor,
            secondary = Color(0xFFB8C7E8),
            onSecondary = Color.Black,
            secondaryContainer = Color(0xFF263248),
            onSecondaryContainer = Color(0xFFEAF0FF),
            tertiary = Color(0xFFFFB4AB),
            onTertiary = Color.Black,
            tertiaryContainer = Color(0xFF5D1915),
            onTertiaryContainer = Color(0xFFFFDAD6),
            background = Color(0xFF050607),
            onBackground = Color(0xFFF4F7FB),
            surface = Color(0xFF101316),
            onSurface = Color(0xFFF4F7FB),
            surfaceVariant = Color(0xFF20252B),
            onSurfaceVariant = Color(0xFFD5DCE5),
            surfaceTint = primaryColor,
            inverseSurface = Color(0xFFF4F7FB),
            inverseOnSurface = Color(0xFF111418),
            error = Color(0xFFFF5A5F),
            onError = Color.White,
            errorContainer = Color(0xFF5D1915),
            onErrorContainer = Color(0xFFFFDAD6),
            border = Color(0xFF7B858F),
            borderVariant = Color(0xFF3A424A),
            scrim = Color.Black
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            onPrimary = Color.Black,
            primaryContainer = Color(0xFFDDE7FF),
            onPrimaryContainer = Color(0xFF07152B),
            inversePrimary = primaryColor,
            secondary = Color(0xFF41516D),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFE4ECFB),
            onSecondaryContainer = Color(0xFF111A2A),
            tertiary = Color(0xFF8B1A1D),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFFFDAD6),
            onTertiaryContainer = Color(0xFF410003),
            background = Color(0xFFF8FAFD),
            onBackground = Color(0xFF15171A),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF15171A),
            surfaceVariant = Color(0xFFE7ECF2),
            onSurfaceVariant = Color(0xFF3E4650),
            surfaceTint = primaryColor,
            inverseSurface = Color(0xFF111418),
            inverseOnSurface = Color(0xFFF4F7FB),
            error = Color(0xFFB3261E),
            onError = Color.White,
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410003),
            border = Color(0xFF5E6872),
            borderVariant = Color(0xFFC3CBD5),
            scrim = Color.Black
        )
    }
}
