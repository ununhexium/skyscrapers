package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.core.requireObject
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import java.io.Writer

class Place(writer:Writer?): MyCliktCommand(writer, name="place") {
  override fun run() = Unit
}