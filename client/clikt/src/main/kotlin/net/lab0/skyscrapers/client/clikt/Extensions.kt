package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.RawOption
import com.github.ajalt.clikt.parameters.options.convert
import net.lab0.skyscrapers.engine.structure.Position
import java.io.Writer

fun RawOption.position() = this.convert { posStr ->
  posStr.split(",")
    .map { it.toInt() }
    .let { Position(it[0], it[1]) }
}
