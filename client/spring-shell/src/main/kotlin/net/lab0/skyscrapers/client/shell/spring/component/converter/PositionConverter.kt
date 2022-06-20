package net.lab0.skyscrapers.client.shell.spring.component.converter

import net.lab0.skyscrapers.api.structure.Position
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class PositionConverter : Converter<String, Position> {
  override fun convert(source: String): Position {
    val split = source.split(",").map { it.toInt() }
    return Position(split[0], split[1])
  }
}
