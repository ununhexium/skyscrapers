package net.lab0.skyscrapers.server.endpoint

import arrow.core.merge
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.ConnectionResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.server.JoiningError
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.endpoint.JoinGame.Examples.exampleResponse
import net.lab0.skyscrapers.server.endpoint.JoinGame.Examples.joiningImpossibleTooManyPlayers
import net.lab0.skyscrapers.server.endpoint.JoinGame.Examples.joiningImpossibleWhenInvalidGameName
import net.lab0.skyscrapers.server.forbidden
import net.lab0.skyscrapers.server.notFound
import org.http4k.contract.ContractRoute
import org.http4k.contract.HttpMessageMeta
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

/**
 * Gives a random token to that connection, to identify it as a user
 */
object JoinGame {

  object Examples {

    val exampleBody = ConnectionResponse(
      0,
      AccessToken("UUID-0123-abcedf")
    )

    val exampleResponse = Response(CREATED).with(
      Body.auto<ConnectionResponse>().toLens() of exampleBody
    )

    val joiningImpossibleTooManyPlayers =
      forbidden("The game foo is full. Can't add any extra player.")

    val joiningImpossibleWhenInvalidGameName =
      notFound("Not found message + indication about which games are available.")

  }

  operator fun invoke(service: Service): ContractRoute {
    val spec = "/games" / Common.gameNamePath / "join" meta {
      summary = "Join a game."
      returning(
        HttpMessageMeta(
          exampleResponse,
          "When joining is accepted.",
          "joinAcceptedResponse",
          null,
          "sky"
        ),
        HttpMessageMeta(
          joiningImpossibleTooManyPlayers,
          "When attempting to join a game that is full.",
          "joinFullGameResponse",
          null,
          "sky"
        ),
        HttpMessageMeta(
          joiningImpossibleWhenInvalidGameName,
          """
            |No game named 'missing'.
            |There are 2 available game. GameName1 GameName2
          """.trimMargin(),
          "joinFullGameResponse",
          null,
          "sky"
        ),
      )
      produces += ContentType.APPLICATION_JSON
      operationId = "joinGameByGameName"
    } bindContract Method.POST

    fun impl(gameName: GameName, _join: String): HttpHandler =
      {
        service.join(gameName)
          .mapLeft {
            when (it) {
              is JoiningError.GameIsFull ->
                // TODO: Forbidden is misleading, find another status
                forbidden("The game ${gameName.value} is full. Can't add any extra player.")
              is JoiningError.GameNotFound -> notFound(it.errors)
              // TODO: the player already joined this game
            }
          }
          .map { playerAndToken ->
            Response(CREATED).with(
              Body.auto<ConnectionResponse>().toLens() of
                  ConnectionResponse(playerAndToken.id, playerAndToken.token)
            )
          }.merge()
      }

    return spec to ::impl
  }
}
