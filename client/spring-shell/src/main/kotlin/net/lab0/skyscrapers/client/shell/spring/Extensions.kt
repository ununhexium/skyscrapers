package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.client.shell.spring.data.PaletteColor
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle


fun AttributedStyle.foreground(color: PaletteColor): AttributedStyle =
  this.foreground(color.value)

fun AttributedStyle.background(color: PaletteColor): AttributedStyle =
  this.background(color.value)

operator fun List<PaletteColor>.get(height: Height): PaletteColor =
  this[height.value]

operator fun AttributedString.plus(other: AttributedString) =
  AttributedStringBuilder.append(this, other)
