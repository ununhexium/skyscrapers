package net.lab0.skyscrapers.client.shell.spring.component

import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.served
import org.http4k.server.Http4kServer
import org.http4k.server.PolyServerConfig
import org.http4k.server.asServer
import org.springframework.stereotype.Component

@Component
class ServerStateComponent(val factory: Http4kServerFactoryComponent) {
  private val service = ServiceImpl.new()
  private var config: PolyServerConfig? = null
  private var server: Http4kServer? = null

  fun startServer(port: Int) {
    config = factory.new(port)
    server = served(service).asServer(config!!).start()
  }

  val isRunning
    get() = server != null

  fun stopServer() {
    config = null
    server = null
  }
}
