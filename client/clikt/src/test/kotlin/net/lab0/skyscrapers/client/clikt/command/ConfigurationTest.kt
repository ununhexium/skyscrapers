package net.lab0.skyscrapers.client.clikt.command

import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import net.lab0.skyscrapers.client.clikt.GameCli
import net.lab0.skyscrapers.client.clikt.KoinBase
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.configuration.DefaultConfig
import net.lab0.skyscrapers.client.clikt.parse
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.koin.test.mock.MockProvider
import org.koin.test.mock.declareMock
import java.io.StringWriter

@TestInstance(PER_CLASS)
internal class ConfigurationTest : KoinBase() {

  @BeforeAll
  fun beforeClass() {
    MockProvider.register { mockkClass(it) }
  }

  @Test
  fun `can read the configuration`() {
    val configurer = declareMock<Configurer>() {
      every { loadConfiguration() } returns DefaultConfig
    }

    val output = StringWriter()
    val cli = GameCli.new(output)

    cli.parse("config")
    output.toString() shouldContain "apiUrl"

    verify {
      configurer.loadConfiguration()
    }
  }

  @Test
  fun `can reset the configuration`() {
    val configurer = declareMock<Configurer>() {
      every { resetConfiguration() } returns Unit
    }

    val output = StringWriter()
    val cli = GameCli.new(output)

    cli.parse("config", "--reset")
    output.toString() shouldContain "Configuration reset."

    verify {
      configurer.resetConfiguration()
    }
  }
}
