package net.lab0.skyscrapers.client.shell.spring.data

import net.lab0.skyscrapers.api.structure.Height
import org.jline.utils.AttributedCharSequence
import java.awt.Color

object DarkPalette : Palette {
  override val backgroundColour = PaletteColor(
    AttributedCharSequence.roundRgbColor(
      0,
      0,
      0,
      256
    )
  )

  fun nearest(color: Color) =
    AttributedCharSequence.roundRgbColor(
      color.red,
      color.green,
      color.blue,
      256
    )

  override fun playerPalette(playerCount: Int): List<PaletteColor> =
    (0 until 360 step (360 / playerCount)).map {
      PaletteColor(
        nearest(
          Color(Color.HSBtoRGB(it.toFloat() / 360, 1.0f, 1.0f))
        )
      )
    }

  // TODO: index bounds check
  override fun heightPalette(maxHeight: Height): List<PaletteColor> =
    (0..maxHeight.value).map {
      PaletteColor(
        nearest(
          Color(
            Color.HSBtoRGB(
              0f,
              0f,
              .5f + (it.toFloat() / maxHeight.value) / 2
            )
          )
        )
      )
    }
}
