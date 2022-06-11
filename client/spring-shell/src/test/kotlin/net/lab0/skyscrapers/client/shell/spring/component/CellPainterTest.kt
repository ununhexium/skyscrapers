package net.lab0.skyscrapers.client.shell.spring.component

import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.client.shell.spring.background
import net.lab0.skyscrapers.client.shell.spring.get
import net.lab0.skyscrapers.client.shell.spring.data.Cell
import net.lab0.skyscrapers.client.shell.spring.data.CellMetadata
import net.lab0.skyscrapers.client.shell.spring.data.DarkPalette
import net.lab0.skyscrapers.client.shell.spring.foreground
import net.lab0.skyscrapers.client.shell.spring.plus
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.junit.jupiter.api.Test

internal class CellPainterTest {
  private val palette = DarkPalette
  private val painter = CellPainter(palette)

  private val maxHeight = Height(3)
  private val playersCount = 3

  private val meta = CellMetadata(maxHeight, playersCount)

  @Test
  fun `color an empty floor cell`() {
    val cell = Cell(Height(0), false, null)
    val text = painter.colorize(cell, meta)

    text shouldBe AttributedString(
      "0",
      AttributedStyle.BOLD_OFF
        .background(palette.backgroundColour)
        .foreground(palette.heightPalette(maxHeight)[Height(0)])
    )
  }

  @Test
  fun `color an empty ceiling cell and make it bold`() {
    val cell = Cell(maxHeight, false, null)
    val text = painter.colorize(cell, meta)

    text shouldBe AttributedString(
      "3",
      AttributedStyle.BOLD
        .background(palette.backgroundColour.value)
        .foreground(palette.heightPalette(maxHeight)[maxHeight])
    )
  }

  @Test
  fun `color a cell with a player`() {
    val playerId = 0
    val height = Height(0)
    val cell = Cell(height, false, playerId)
    val text = painter.colorize(cell, meta)

    text shouldBe AttributedString(
      "A",
      AttributedStyle.BOLD_OFF
        .background(palette.backgroundColour)
        .foreground(palette.playerPalette(playersCount)[playerId])
    ) + AttributedString(
      "0",
      AttributedStyle.BOLD_OFF
        .background(palette.backgroundColour)
        .foreground(palette.heightPalette(maxHeight)[height])
    )
  }

  @Test
  fun `color a sealed cell`() {
    val cell = Cell(Height(0), true, null)
    val text = painter.colorize(cell, meta)

    text shouldBe AttributedString(
      "██",
      AttributedStyle.BOLD_OFF
        .background(palette.backgroundColour)
        .foreground(palette.heightPalette(maxHeight)[0])
    )
  }
}
