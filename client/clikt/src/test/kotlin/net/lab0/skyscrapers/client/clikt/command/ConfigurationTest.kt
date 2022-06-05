package net.lab0.skyscrapers.client.clikt.command

import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.lab0.skyscrapers.client.clikt.GameCli
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.configuration.DefaultConfig
import net.lab0.skyscrapers.client.clikt.parse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.io.StringWriter

internal class ConfigurationTest : KoinTest {

  @BeforeEach
  fun beforeEach() {
    startKoin {}
  }

  @AfterEach
  fun afterEach() {
    stopKoin()
  }

  @Test
  fun `can read the configuration`() {
    val configurer = mockk<Configurer>()
    every { configurer.loadConfiguration() } returns DefaultConfig
    val output = StringWriter()

    val cli = GameCli.new(output, configurer = configurer)
    cli.parse("config")
    output.toString() shouldContain "apiUrl"

    verify {
      configurer.loadConfiguration()
    }
  }

  @Test
  fun `can reset the configuration`() {
    val configurer = mockk<Configurer>()
    every { configurer.resetConfiguration() } returns Unit
    val output = StringWriter()

    val cli = GameCli.new(output, configurer)
    cli.parse("config", "--reset")
    output.toString() shouldContain "Configuration reset."

    verify {
      configurer.resetConfiguration()
    }
  }
}
