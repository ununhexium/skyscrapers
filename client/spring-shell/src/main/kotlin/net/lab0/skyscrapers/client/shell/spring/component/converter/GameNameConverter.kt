package net.lab0.skyscrapers.client.shell.spring.component.converter

import net.lab0.skyscrapers.api.dto.value.GameName
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class GameNameConverter: Converter<String, GameName> {
  override fun convert(source: String): GameName =
    GameName(source)
}
