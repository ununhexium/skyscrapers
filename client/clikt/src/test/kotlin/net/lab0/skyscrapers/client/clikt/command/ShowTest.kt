package net.lab0.skyscrapers.client.clikt.command

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.string.shouldContain
import net.lab0.skyscrapers.client.ServerIntegrationTest
import net.lab0.skyscrapers.client.clikt.GameCli
import net.lab0.skyscrapers.client.clikt.parse
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.client.OkHttp
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class ShowTest: ServerIntegrationTest {
  val client = SkyscraperClientImpl(OkHttp())

  @Disabled // TODO: continue
  @Test
  fun `can show a game`() {
    useServer { url ->
      val menu = client.status(url).shouldBeRight()
      val writer = StringWriter()
      val cli = GameCli.new(writer, skyscraperClient = client)

      val fubar = GameName("fubar")
      cli.parse("new-game", "--named", fubar.value)
      cli.parse("join", "--game", fubar.value)
      cli.parse("show", "--game", fubar.value)

      writer.toString() shouldContain "Game at turn 0"
    }
  }
}