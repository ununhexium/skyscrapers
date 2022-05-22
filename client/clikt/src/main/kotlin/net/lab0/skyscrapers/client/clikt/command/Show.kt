package net.lab0.skyscrapers.client.clikt.command

import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.http.SkyscraperClient
import java.io.Writer

//class Show(writer: Writer?, val configurer: Configurer, val skyscraperClient: SkyscraperClient) :
//  MyCliktCommand(writer, name = "show") {
//  override fun run() {
//    skyscraperClient.status(configurer.loadConfiguration().server.apiUrl)
//      .mapLeft { myEcho(it.description) }
//      .map { myEcho(it) }
//  }
//}
