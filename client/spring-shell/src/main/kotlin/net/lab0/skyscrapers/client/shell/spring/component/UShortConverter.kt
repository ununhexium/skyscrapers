package net.lab0.skyscrapers.client.shell.spring.component

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class UShortConverter : Converter<String, UShort> {
  override fun convert(source: String) =
    source.toUShort()
}
