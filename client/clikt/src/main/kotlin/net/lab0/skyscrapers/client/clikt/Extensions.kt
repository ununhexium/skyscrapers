package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.parameters.options.RawOption
import com.github.ajalt.clikt.parameters.options.convert
import net.lab0.skyscrapers.api.structure.Position

// TODO: bug: if this is not inline, it breaks at runtime with
// java.lang.NoSuchMethodError: 'com.github.ajalt.clikt.parameters.options.OptionWithValues net.lab0.skyscrapers.client.clikt.ExtensionsKt.position(com.github.ajalt.clikt.parameters.options.OptionWithValues)'
@Suppress("NOTHING_TO_INLINE")
inline fun RawOption.position() = this.convert { posStr ->
  posStr.split(",")
    .map { it.toInt() }
    .let { Position(it[0], it[1]) }
}
