package net.lab0.skyscrapers.client.clikt.command

import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import java.io.Writer

class Place(writer:Writer?): MyCliktCommand(writer, name="place") {
  override fun run() = Unit
}