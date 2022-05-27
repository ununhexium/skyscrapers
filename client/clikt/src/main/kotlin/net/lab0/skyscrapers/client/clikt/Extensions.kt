package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.parameters.options.RawOption
import com.github.ajalt.clikt.parameters.options.convert
import net.lab0.skyscrapers.api.structure.Position

fun RawOption.position() = this.convert { posStr ->
  posStr.split(",")
    .map { it.toInt() }
    .let { Position(it[0], it[1]) }
}
