package net.lab0.skyscrapers.server.endpoint

import arrow.core.merge
import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.server.Service
import org.http4k.contract.ContractRoute
import org.http4k.contract.HttpMessageMeta
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

object NewGame {
  val exampleDTO = GameResponse(
    GameName("foo"),
    GameFactoryImpl().new().state,
  )

  val exampleResponse = Response(Status.OK).with(
    Body.auto<GameResponse>().toLens() of exampleDTO
  )

  operator fun invoke(service: Service): ContractRoute {
    val spec = "/games" / Common.gameNamePath meta {
      summary = "Create a new game."
      returning(
        HttpMessageMeta(
          exampleResponse,
          "The initial state for a game with default parameters.",
          "gameInitResponse",
          null, // TODO: Json Schema
          "sky"
        )
      )
      produces += ContentType.APPLICATION_JSON
      operationId = "createGameState"
    } bindContract Method.POST

    fun impl(gameName: GameName): HttpHandler =
      { req ->
        service.getGame(gameName)
          // here this is actually the case we want -> no game exists, and we can crate it.
          .mapLeft {
            val new = service.createGame(gameName)
            Response(Status.CREATED).with(
              Body.auto<GameResponse>().toLens() of
                  GameResponse(gameName, new.state)
            )
          }
          .map {
            Response(Status.BAD_REQUEST).with(
              Body.auto<ErrorResponse>().toLens() of
                  ErrorResponse("The game $gameName already exists.")
            )
          }.merge()
      }

    return spec to ::impl
  }
}
