package net.lab0.skyscrapers.client.clikt.command

import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Writer

class Version(writer: Writer?) :
  MyCliktCommand(writer, name = "version"),
  KoinComponent {

  override fun run() {
    myEcho("0.0.0") // TODO: inject version
  }
}
