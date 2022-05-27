package net.lab0.skyscrapers.server.filter

import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.badRequest
import net.lab0.skyscrapers.server.AUTHORIZATION
import net.lab0.skyscrapers.server.pathGameName
import org.http4k.core.Body
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.Header

class GameAccessFilter(val service: Service) : Filter {

  override fun invoke(next: HttpHandler): HttpHandler {
    return { req ->
      doAuth(service, next, req)
    }
  }

  fun doAuth(
    service: Service,
    next: (Request) -> Response,
    req: Request,
  ): Response {
    val auth = Header.AUTHORIZATION.extract(req)

    val game = req.pathGameName()
      ?: return badRequest("The game name wasn't specified: ${req.uri}")


    return if (service.canPlay(game, auth.token)) {
      next(req)
    } else {
      Response(Status.UNAUTHORIZED).with(
        Body.auto<ErrorResponse>().toLens() of
            ErrorResponse(
              "Failed to authenticate: the player can't play on the game ${game.value}"
            )
      )
    }
  }
}
