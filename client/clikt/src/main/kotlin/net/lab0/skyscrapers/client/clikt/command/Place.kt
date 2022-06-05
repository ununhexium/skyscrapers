package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.clikt.struct.LastGame
import net.lab0.skyscrapers.client.http.ClientError
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Writer
import kotlin.io.path.inputStream

class Place(writer: Writer?) :
  MyCliktCommand(    writer,    name = "place"  ),
KoinComponent {
  private val game by option("-g", "--game", help = "The game to play on")
    .convert { GameName(it) }
    .required()

  private val sky by inject<SkyscraperClient>()

  private val position by option(
    "-a",
    "--at",
    help = "Where to place the builder. Syntax: --at x,y"
  )
    .convert { coordinates ->
      coordinates
        .split(",")
        .map { it.toInt() }
        .let { Position(it[0], it[1]) }
    }
    .required()

  override fun run() {

    val lastGame = Json.decodeFromStream<LastGame>(
      Constants.lastJoin(game).inputStream()
    )

    sky.place(
      GameName(lastGame.gameName),
      AccessToken(lastGame.token),
      position
    ).map {
      myEcho("Placed a builder at ${position.x},${position.y}")
    }.mapLeft { err ->
      when (err) {
        is ClientError.GameRuleErrors -> {
          myEcho("Game rule violated.")
          myEcho("")
          err.violations.forEach { v ->
            myEcho("Name: ${v.name}")
            myEcho("Description: ${v.description}")
            myEcho("Detail: ${v.detail}")
          }
        }
        is ClientError.SimpleErrors -> err.errors.forEach { it -> myEcho(it) }
      }
    }
  }
}
