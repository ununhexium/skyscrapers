package net.lab0.skyscrapers.server.endpoint

import arrow.core.merge
import net.lab0.skyscrapers.api.dto.ConnectionResponse
import net.lab0.skyscrapers.server.JoiningError
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.forbidden
import net.lab0.skyscrapers.server.notFound
import net.lab0.skyscrapers.server.withGame
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

/**
 * Gives a random token to that connection, to identify it as a user
 */
class JoinGame(val service: Service) : HttpHandler {
  override fun invoke(req: Request) =
    withGame(req, service) { (gameName, _) ->
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
}
