package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.path


fun Request.pathGameName() =
  this.path("gameName")?.let(::GameName)

fun notFound(vararg messages: String) =
  Response(NOT_FOUND).with(
    Body.auto<ErrorResponse>().toLens() of
        ErrorResponse(messages.toList())
  )
