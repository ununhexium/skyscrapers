package net.lab0.skyscrapers.client.shell.spring.data

import net.lab0.skyscrapers.api.structure.Height
import java.awt.Color

object DarkPalette : Palette {
  override val backgroundColour = PaletteColor(Color.BLACK.rgb)

  override fun playerPalette(playerCount: Int): List<PaletteColor> =
    (0 until 360 step 360 / playerCount).map {
      PaletteColor(
        Color.HSBtoRGB(it.toFloat(), 1.0f, 1.0f)
      )
    }

  override fun heightPalette(maxHeight: Height): List<PaletteColor> =
    (0..maxHeight.value).map {
      PaletteColor(
        Color.HSBtoRGB(0f, 0f, .5f + (it.toFloat() / maxHeight.value) / 2)
      )
    }
}