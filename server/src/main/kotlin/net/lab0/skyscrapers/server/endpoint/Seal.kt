package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.BuildTurnDTO
import net.lab0.skyscrapers.api.dto.GameStateDTO
import net.lab0.skyscrapers.api.dto.GameViolationDTO
import net.lab0.skyscrapers.api.dto.GameViolationsDTO
import net.lab0.skyscrapers.api.dto.PlaceTurnDTO
import net.lab0.skyscrapers.api.dto.SealTurnDTO
import net.lab0.skyscrapers.engine.exception.GameRuleViolationException
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.badRequest
import net.lab0.skyscrapers.server.notFound
import net.lab0.skyscrapers.server.pathGameName
import net.lab0.skyscrapers.server.toModel
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

fun seal(service: Service, req: Request): Response {
  val gameName = req.pathGameName()
    ?: return badRequest("Must specify the name of the game: /api/v1/games/{game_name}/seal")

  val game = service.getGame(gameName)
    ?: return notFound("The game '$gameName' was not found.")

  val turn = Body.auto<SealTurnDTO>().toLens().extract(req)

  return try {
    // TODO: add test if not authed in the game -> 403
    val turn1 = turn.toModel(gameName, service)
    game.play(turn1!!)
    Response(Status.CREATED).with(
      Body.auto<GameStateDTO>().toLens() of GameStateDTO(game.state)
    )
  } catch (e: GameRuleViolationException) {
    Response(Status.CONFLICT).with(
      Body.auto<GameViolationsDTO>().toLens() of GameViolationsDTO(
        e.violations.map { GameViolationDTO(it) }
      )
    )
  }
}
