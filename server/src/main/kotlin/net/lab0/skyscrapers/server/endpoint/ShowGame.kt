package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.withGame
import org.http4k.contract.ContractRoute
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.Path

object ShowGame {
  operator fun invoke(service: Service): ContractRoute {
    val gameNamePath = Path.map(::GameName).of("gameName")

    val spec = "/games" / gameNamePath meta {
      summary = "Get the current state of a game."
      produces += ContentType.APPLICATION_JSON
      // TODO: complete the meta info
    } bindContract Method.GET

    return spec to { gameName: GameName ->
      {
        withGame(gameName, service) { game ->
          Response(OK).with(
            Body.auto<GameResponse>().toLens() of GameResponse(gameName, game.state)
          )
        }
      }
    }
  }
}
