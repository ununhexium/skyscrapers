package net.lab0.skyscrapers.client.clikt

import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.di.cliModule
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

fun main(argv: Array<String>) {
  startKoin {
    modules(
      cliModule,
      module {
        single {
          ClientFilters
            .SetBaseUriFrom(
              Uri.of(get<Configurer>().loadConfiguration().server.apiUrl)
            ).then(OkHttp())
        }
      }
    )
  }

  GameCli.new(null).main(argv)
  
  stopKoin()
}
