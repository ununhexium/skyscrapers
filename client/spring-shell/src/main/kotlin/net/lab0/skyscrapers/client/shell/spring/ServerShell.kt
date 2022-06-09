package net.lab0.skyscrapers.client.shell.spring

import net.lab0.skyscrapers.client.shell.spring.component.ServerStateComponent
import net.lab0.skyscrapers.server.routed
import org.http4k.server.asServer
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@ShellComponent
class ServerShell(val state:ServerStateComponent) {

  @ShellMethod(
    "Start a server",
    key = ["start-server"]
  )
  fun startServer(
    @Min(0)
    @Max(0xffff)
    @ShellOption("--port", help = "The server port")
    port: Int,
  ): String? {
    if(state.isRunning) return "The server is already started."

    state.startServer(port)
    return "Server started on port $port"
  }

  @ShellMethod(
    "Stop the server",
    key = ["stop-server"]
  )
  fun stopServer(): String? {
    if(!state.isRunning) return "The server is already stopped."

    state.stopServer()
    return "Server stopped."
  }
}
