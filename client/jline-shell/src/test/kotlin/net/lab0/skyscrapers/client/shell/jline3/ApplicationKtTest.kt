package net.lab0.skyscrapers.client.shell.jline3

import org.assertj.core.api.Assertions.assertThat
import org.jline.terminal.Terminal
import org.junit.jupiter.api.Test

internal class ApplicationKtTest {

  @Test
  fun `can quit the app`() {
    val app = Application.new()

    val t = Thread { app.run() }

    val terminal = app.terminal
    terminal.flush()
    terminal.raise(Terminal.Signal.INT)
    terminal.flush()

    t.join()

    assertThat(app.running).isFalse()
  }

  @Test
  fun `can run the help command`() {
    val app = Application.new()

    val t = Thread { app.run() }

    val terminal = app.terminal
    terminal.flush()
    val input = terminal.input()
    terminal.writer().println("help")

    terminal.raise(Terminal.Signal.QUIT)

    t.join()

    assertThat(input.bufferedReader().readLines()).isEqualTo("help")

  }
}
