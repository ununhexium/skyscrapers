package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.TurnTypeDTO
import net.lab0.skyscrapers.server.notFound
import net.lab0.skyscrapers.server.pathGameName
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

fun playGame(service: Service, req: Request): Response {
  val gameName = req.pathGameName()
    ?: return notFound("Must specify the name of the game: /api/v1/games/<gameName>")

  val game = service.getGame(gameName)
    ?:return notFound("The game '$gameName' was not found.")

  val turn = Body.auto<TurnTypeDTO>().toLens().extract(req)
  game.play(turn.toTurnType())

  return Response(Status.OK).with(
    Body.auto<GameResponse>().toLens() of GameResponse(gameName, game.state)
  )
}
