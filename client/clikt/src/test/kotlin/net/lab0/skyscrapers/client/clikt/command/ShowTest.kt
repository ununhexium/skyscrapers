package net.lab0.skyscrapers.client.clikt.command

import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import net.lab0.skyscrapers.client.ServerIntegrationTest
import net.lab0.skyscrapers.client.clikt.GameCli
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.parse
import net.lab0.skyscrapers.server.value.GameName
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class ShowTest: ServerIntegrationTest {

  @Disabled // TODO: continue
  @Test
  fun `can show a game`() {
    useServer { url ->
      val configurer = mockk<Configurer>()
      every { configurer.loadConfiguration().server.apiUrl } returns url
      val writer = StringWriter()
      val cli = GameCli.new(writer)

      val fubar = GameName("fubar")
      cli.parse("new-game", "--named", fubar.value)
      cli.parse("join", "--game", fubar.value)
      cli.parse("show", "--game", fubar.value)

      writer.toString() shouldContain "Game at turn 0"
    }
  }
}
