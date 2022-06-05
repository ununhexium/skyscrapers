package net.lab0.skyscrapers.client.clikt.command

import io.kotest.matchers.string.shouldContain
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.client.FakeServerTest
import net.lab0.skyscrapers.client.clikt.GameCliFactory
import net.lab0.skyscrapers.client.clikt.parse
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class ShowTest : FakeServerTest {
  @Test
  fun `can show a game`() {
    fakeServer { handler ->
      val writer = StringWriter()
      val cli = GameCliFactory().new(writer, handler = handler)

      val fubar = GameName("fubar")
      cli.parse("new-game", fubar.value)
//      cli.parse("join", fubar.value)
//      cli.parse("show", fubar.value)

      writer.toString() shouldContain "Created game 'fubar'"
    }
  }
}
