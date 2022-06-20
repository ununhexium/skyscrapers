package net.lab0.skyscrapers.client.shell.spring.data

import net.lab0.skyscrapers.api.structure.Height

interface Palette {
  val errorForeground: PaletteColor
  val backgroundColour: PaletteColor
  fun playerPalette(playerCount: Int): List<PaletteColor>
  fun heightPalette(maxHeight: Height): List<PaletteColor>
}
