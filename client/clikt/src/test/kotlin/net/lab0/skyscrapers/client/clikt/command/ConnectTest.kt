package net.lab0.skyscrapers.client.clikt.command

import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import net.lab0.skyscrapers.client.ServerIntegrationTest
import net.lab0.skyscrapers.client.clikt.GameCli
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.parse
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class ConnectTest : ServerIntegrationTest {
  @Test
  fun `can connect to a server`() {
    useServer { url ->
      val output = StringWriter()
      val configurer = mockk<Configurer>()
      every { configurer.loadConfiguration().server.apiUrl } returns url

      val cli = GameCli.new(output, configurer = configurer)
      cli.parse("connect")

      output.toString() shouldContain "Connected to $url"
    }
  }
}
