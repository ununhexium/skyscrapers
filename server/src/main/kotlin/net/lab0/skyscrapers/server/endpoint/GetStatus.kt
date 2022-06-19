package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.server.Service
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

class GetStatus(val service: Service) : HttpHandler {
  override fun invoke(req: Request) =
    Response(Status.OK).with(
      Body
        .auto<StatusResponse>().toLens() of
          StatusResponse.of("up", service.getGameNames())
    )
}
