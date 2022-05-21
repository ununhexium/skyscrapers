package net.lab0.skyscrapers.client.clikt

import io.kotest.matchers.string.shouldNotContain
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.routed
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class GameCliTest {
  @Test
  fun `connection integration test`() {
    val service = ServiceImpl.new()
    // TODOL these constructs should move to some test module
    val server = routed(service).asServer(Undertow()).start()
    val writer = StringWriter()

    val cli = GameCli.new(writer)
    cli.parse("config", "--reset")
    cli.parse("connect")

    writer.toString() shouldNotContain "Failed to connect to the server"

    server.stop()
  }
}