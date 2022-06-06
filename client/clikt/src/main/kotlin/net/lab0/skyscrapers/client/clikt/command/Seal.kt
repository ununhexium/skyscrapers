package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position.Style.COMA
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.clikt.position
import net.lab0.skyscrapers.client.clikt.struct.LastGame
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Writer
import kotlin.io.path.inputStream

class Seal(writer: Writer?) :
  MyCliktCommand(writer, name = "seal"),
  KoinComponent {
  private val game by option("-g", "--game", help = "The game to play on")
    .convert { GameName(it) }
    .required()

  private val sky by inject<SkyscraperClient>()

  private val start by option(
    "-f",
    "--from",
    help = "Which builder to move. Syntax: --from x,y"
  )
    .position()
    .required()

  private val target by option(
    "-t",
    "--to",
    help = "Where to move the builder. Syntax: --to x,y"
  )
    .position()
    .required()

  private val build by option(
    "-s",
    "--seal",
    help = "Where to seal. Syntax: --seal x,y"
  )
    .position()
    .required()

  override fun run() {

    val lastGame = Json.decodeFromStream<LastGame>(
      Constants.lastJoin(game).inputStream()
    )

    sky.seal(
      GameName(lastGame.gameName),
      AccessToken(lastGame.token),
      start,
      target,
      build,
    ).map {
      myEcho(
        "Moved builder from ${start.toString(COMA)} " +
            "to ${target.toString(COMA)} " +
            "and sealed at ${build.toString(COMA)}."
      )
    }.mapLeft(::showError)
  }
}
