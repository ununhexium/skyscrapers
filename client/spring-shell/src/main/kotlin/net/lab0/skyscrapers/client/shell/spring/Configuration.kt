package net.lab0.skyscrapers.client.shell.spring

import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.springframework.context.annotation.Bean

@org.springframework.context.annotation.Configuration
class Configuration {
  @Bean
  fun handler(): HttpHandler =
    OkHttp()
}
