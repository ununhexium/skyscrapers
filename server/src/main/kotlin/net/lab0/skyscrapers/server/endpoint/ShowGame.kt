package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.BlocksData
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.api.structure.Player
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.endpoint.ShowGame.Examples.notFound
import net.lab0.skyscrapers.server.endpoint.ShowGame.Examples.ok
import net.lab0.skyscrapers.server.withGame
import org.http4k.contract.ContractRoute
import org.http4k.contract.HttpMessageMeta
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

object ShowGame {

  object Examples {

    val ok = Response(OK).with(
      Body.auto<GameResponse>().toLens() of GameResponse(
        GameName("foo"),
        GameState.from(
          listOf(Player(0, true), Player(1, false)),
          2,
          BlocksData(
            Height(0) to 15,
            Height(1) to 10,
            Height(2) to 5,
          ),
          """
          |0 1 2
          |0 1 0
        """.trimMargin(),
          """
          |false true false
          |false false false
        """.trimMargin(),
          """
          |0 . 1
          |1 0 .
        """.trimMargin()
        )
      )
    )

    val notFound = Response(NOT_FOUND).with(
      Body.auto<ErrorResponse>().toLens() of
          ErrorResponse("No game named 'missing'. There is 0 available game.")
    )
  }

  operator fun invoke(service: Service): ContractRoute {
    val spec = "/games" / Common.gameNamePath meta {
      summary = "Get the current state of a game."
      description = """
        |Returns the latest state of the game if it exists or 
        |404 Not Found otherwise.
      """.trimMargin()
      returning(
        HttpMessageMeta(
          ok,
          "OK",
          "gameResponse",
          null,
          "sky"
        ),
        HttpMessageMeta(
          notFound,
          "Not Found",
          "notFound",
          null,
          "sky"
        )
      )
      produces += ContentType.APPLICATION_JSON
      operationId = "getGameStateByGameName"
    } bindContract Method.GET

    fun impl(gameName: GameName): HttpHandler =
      {
        withGame(gameName, service) { game ->
          Response(OK).with(
            Body.auto<GameResponse>().toLens() of GameResponse(
              gameName,
              game.state
            )
          )
        }
      }

    return spec to ::impl
  }
}
