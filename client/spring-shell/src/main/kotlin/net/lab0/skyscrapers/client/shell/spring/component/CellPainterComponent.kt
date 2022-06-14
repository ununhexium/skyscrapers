package net.lab0.skyscrapers.client.shell.spring.component

import net.lab0.skyscrapers.client.shell.spring.background
import net.lab0.skyscrapers.client.shell.spring.data.Cell
import net.lab0.skyscrapers.client.shell.spring.data.CellMetadata
import net.lab0.skyscrapers.client.shell.spring.data.Palette
import net.lab0.skyscrapers.client.shell.spring.foreground
import net.lab0.skyscrapers.client.shell.spring.get
import net.lab0.skyscrapers.client.shell.spring.plus
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.stereotype.Component

@Component
class CellPainterComponent(private val palette: Palette) {
  fun colorize(cell: Cell, meta: CellMetadata): AttributedString {
    return if (cell.sealed) {
      getSealedString(meta)
    } else {
      getPlayerString(cell, meta) + getHeightString(cell, meta)
    }
  }

  private fun getSealedString(
    meta: CellMetadata
  ) = AttributedString(
    "██",
    AttributedStyle.BOLD_OFF
      .background(palette.backgroundColour)
      .foreground(palette.heightPalette(meta.maxHeight)[0])
  )

  private fun getPlayerString(
    cell: Cell,
    meta: CellMetadata
  ): AttributedString =
    cell.player?.let { p ->
      AttributedString(
        ('A' + p).toString(),
        AttributedStyle.BOLD_OFF
          .background(palette.backgroundColour)
          .foreground(palette.playerPalette(meta.playersCount)[p])
      )
    } ?: AttributedString.EMPTY

  private fun getHeightString(
    cell: Cell,
    meta: CellMetadata
  ) = AttributedString(
    cell.height.value.toString(),
    getHeightStyle(meta, cell)
  )

  private fun getHeightStyle(
    meta: CellMetadata,
    cell: Cell
  ) =
    if (cell.height == meta.maxHeight) {
      AttributedStyle.BOLD
    } else {
      AttributedStyle.BOLD_OFF
    }
      .background(palette.backgroundColour)
      .foreground(palette.heightPalette(meta.maxHeight)[cell.height])
}
