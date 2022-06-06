package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.springframework.stereotype.Component

@Component
class SkyscraperClientFactoryComponent(val handler: HttpHandler) {
  fun newClient(baseUrl: BaseUrl): SkyscraperClient {
    return SkyscraperClientImpl(
      ClientFilters
        .SetBaseUriFrom(Uri.of(baseUrl.value)).then(handler)
    )
  }
}
