package net.lab0.skyscrapers.client.clikt.command

import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import org.koin.core.component.KoinComponent
import java.io.Writer

class Version(writer: Writer?) :
  MyCliktCommand(writer, name = "version"),
  KoinComponent {

  override fun run() {
    myEcho("0.0.0") // TODO: inject version
  }
}
