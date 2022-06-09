package net.lab0.skyscrapers.client.shell.spring.component

import org.http4k.server.ServerConfig
import org.http4k.server.Undertow
import org.springframework.stereotype.Component

@Component
class Http4kServerFactoryComponent {
  fun new(port:Int): ServerConfig =
    Undertow(port)
}
