package net.lab0.skyscrapers.client.shell.spring.component

import net.lab0.skyscrapers.api.dto.value.GameName
import org.springframework.core.convert.converter.Converter

class GameNameConverter: Converter<String, GameName> {
  override fun convert(source: String): GameName =
    GameName(source)
}
