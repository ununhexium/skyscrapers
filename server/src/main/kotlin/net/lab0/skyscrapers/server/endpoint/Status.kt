package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.dto.StatusResponse
import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

fun status(service: Service) =
  Response(Status.OK).with(
    Body
      .auto<StatusResponse>().toLens() of
        StatusResponse.of("up", service.getGameNames())
  )