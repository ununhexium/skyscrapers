package net.lab0.skyscrapers.server

import org.http4k.lens.Path
import org.http4k.routing.bind
import org.http4k.routing.websockets
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage

fun socketed(service: Service) =
  websockets(
    "/api/v1/games/{gameName}/ws" bind { ws: Websocket ->
        val name = Path.of("name")
        ws.onMessage {
          ws.send(WsMessage("hi!"))
        }
        ws.onClose { println("closed") }
        ws.send(WsMessage("hello $name"))
      }
    )
