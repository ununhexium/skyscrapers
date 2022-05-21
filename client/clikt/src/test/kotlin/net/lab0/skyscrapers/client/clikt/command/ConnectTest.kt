package net.lab0.skyscrapers.client.clikt.command

import arrow.core.Either
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.lab0.skyscrapers.client.clikt.GameCli
import net.lab0.skyscrapers.client.clikt.parse
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.http.SkyscraperMenuClient
import net.lab0.skyscrapers.server.value.GameName
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class ConnectTest {
  @Test
  fun `can connect to a server without games`() {
    val output = StringWriter()
    val skyClient = mockk<SkyscraperClient>()
    val skyMenuClient = mockk<SkyscraperMenuClient>()
    every { skyClient.connect(any()) } returns Either.Right(skyMenuClient)
    every { skyMenuClient.listGames() } returns listOf()

    val cli = GameCli.new(output, skyscraperClient = skyClient)
    cli.parse("connect")

    verify {
      skyClient.connect(any())
      skyMenuClient.listGames()
    }

    output.toString() shouldContain "The server has no games."
  }

  @Test
  fun `can connect to a server with games`() {
    val output = StringWriter()
    val skyClient = mockk<SkyscraperClient>()
    val skyMenuClient = mockk<SkyscraperMenuClient>()
    every { skyClient.connect(any()) } returns Either.Right(skyMenuClient)
    every { skyMenuClient.listGames() } returns listOf(GameName("fubar"))

    val cli = GameCli.new(output, skyscraperClient = skyClient)
    cli.parse("connect")

    verify {
      skyClient.connect(any())
      skyMenuClient.listGames()
    }

    output.toString() shouldContain "fubar"
  }
}