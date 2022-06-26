package net.lab0.skyscrapers.server.endpoint

import arrow.core.merge
import net.lab0.skyscrapers.api.dto.HistoryResponseDTO
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.engine.action.DSL
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.endpoint.History.Examples.ok
import org.http4k.contract.ContractRoute
import org.http4k.contract.HttpMessageMeta
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

object History {

  object Examples {

    private val game = GameFactoryImpl().newNano().also {
      // placement
      it.play(DSL.player(0).placement.addBuilder(0, 0))
      it.play(DSL.player(1).placement.addBuilder(1, 1))

      // movement
      it.play(DSL.player(0).building.move().from(0, 0).to(0, 1).andBuild(0, 0))
      it.play(DSL.player(1).building.move().from(1, 1).to(0, 0).andBuild(1, 1))

      it.play(DSL.player(0).building.move().from(0, 1).to(1, 1).andBuild(0, 1))
      it.play(DSL.player(1).building.move().from(0, 0).to(0, 1).andBuild(0, 0))

      it.play(DSL.player(0).building.giveUp())
    }

    val okHistory = game.history

    val ok = Response(OK).with(
      Body.auto<HistoryResponseDTO>().toLens() of
          HistoryResponseDTO.from(game.history)
    )
  }

  operator fun invoke(service: Service): ContractRoute {
    val spec = "/games" / Common.gameNamePath / "history" meta {
      summary = "Get the history of the game."
      returning(
        HttpMessageMeta(
          ok,
          "The whole history of the game.",
          "fullGameHistory",
          null,
        )
      )
      produces += ContentType.APPLICATION_JSON
      operationId = "getGameHistoryByGameName"
    } bindContract Method.GET

    fun impl(gameName: GameName, _history: String): HttpHandler =
      {
        service.getGameHistory(gameName)
          .map {
            Response(OK).with(
              Body.auto<HistoryResponseDTO>().toLens() of
                  HistoryResponseDTO.from(it)
            )
          }
          .mapLeft {
            TODO("History doesnt exist")
          }
          .merge()

      }

    return spec to ::impl
  }

}
