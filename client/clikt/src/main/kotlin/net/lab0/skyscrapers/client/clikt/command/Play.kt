package net.lab0.skyscrapers.client.clikt.command

import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import java.io.Writer

class Play(writer: Writer?) : MyCliktCommand(writer, name = "play") {
  override fun run() = Unit
}
