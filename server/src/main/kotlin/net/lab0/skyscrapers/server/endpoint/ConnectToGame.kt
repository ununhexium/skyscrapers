package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.dto.ConnectionResponse
import net.lab0.skyscrapers.server.dto.ErrorResponse
import net.lab0.skyscrapers.server.exception.GameFullException
import net.lab0.skyscrapers.server.pathGameName
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

/**
 * Gives a random token to that connection, to identify it as a user
 */
fun connectToGame(service: Service, req: Request): Response {
  val gameName = req.pathGameName()

  return if (gameName == null) {
    Response(NOT_FOUND)
  } else {
    val game = service.getGame(gameName)
    if (game == null) {
      Response(NOT_FOUND)
    } else {
      try {
        val cnx = service.connect(gameName)
        Response(CREATED).with(
          Body.auto<ConnectionResponse>().toLens() of
              ConnectionResponse(cnx.player, cnx.token)
        )
      } catch (e: GameFullException) {
        Response(FORBIDDEN).with(
          Body.auto<ErrorResponse>().toLens() of
              ErrorResponse(e.message ?: "No error message")
        )
      }
    }
  }
}
