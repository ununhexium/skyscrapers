package net.lab0.skyscrapers.client.shell.jline3.execution

import com.github.ajalt.clikt.parameters.options.RawOption
import com.github.ajalt.clikt.parameters.options.convert
import net.lab0.skyscrapers.logic.structure.Position

fun RawOption.position() = this.convert { posStr ->
  posStr.split(",")
    .map { it.toInt() }
    .let { Position(it[0], it[1]) }
}

