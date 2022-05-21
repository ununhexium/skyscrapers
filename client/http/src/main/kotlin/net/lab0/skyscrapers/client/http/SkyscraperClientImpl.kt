package net.lab0.skyscrapers.client.http

import arrow.core.Either
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.Uri
import org.http4k.core.extend

private operator fun Uri.div(s: String): Uri =
  this.extend(Uri.of(s))

class SkyscraperClientImpl(val handler: HttpHandler) : SkyscraperClient {
  override fun connect(url: String): Either<Status, SkyscraperMenuClient> {
    val apiUrl = Uri.of(url) / "v1"

    val req = Request(Method.GET, apiUrl / "status")
    val res = handler(req)
    return if (res.status == Status.OK) {
      Either.Right(SkyscraperMenuClientImpl(apiUrl))
    } else {
      Either.Left(res.status)
    }
  }
}
