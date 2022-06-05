package net.lab0.skyscrapers.client.clikt.command

import io.kotest.matchers.string.shouldContain
import net.lab0.skyscrapers.client.FakeServerTest
import net.lab0.skyscrapers.client.clikt.GameCliFactory
import net.lab0.skyscrapers.client.clikt.KoinBase
import net.lab0.skyscrapers.client.clikt.parse
import org.junit.jupiter.api.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.io.StringWriter

internal class ConnectTest : KoinBase(), FakeServerTest {

  @Test
  fun `can connect to a server`() {
    fakeServer { handler ->

      loadKoinModules(
        module { single { handler } }
      )

      val output = StringWriter()

      val cli = GameCliFactory().new(output)
      cli.parse("connect")

      output.toString() shouldContain "Connected."
    }
  }
}
