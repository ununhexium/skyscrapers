package net.lab0.skyscrapers.client.shell.jline3

import org.jline.builtins.Completers.OptDesc
import org.jline.builtins.Completers.OptionCompleter
import org.jline.builtins.Completers.TreeCompleter.Node
import org.jline.console.CommandRegistry
import org.jline.reader.Completer
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.reader.impl.completer.AggregateCompleter
import org.jline.reader.impl.completer.ArgumentCompleter
import org.jline.reader.impl.completer.NullCompleter
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

fun main(args: Array<String>) {
  val terminal = TerminalBuilder.builder().build()
  Application(terminal).run()
}

class Application(val terminal: Terminal) {

  var running = true

  fun run() {

    val registry = GameCommands()

    val parser = DefaultParser()
    val reader = LineReaderBuilder
      .builder()
      .terminal(terminal)
      .parser(parser)
      .completer(
        AggregateCompleter(
          buildNewGameCommands(),
          StringsCompleter("restart")
        )
      )
      .build()

    running = true
    while (running) {
      try {
        val line = reader.readLine("prompt > ")
        println(line)
      } catch (e: UserInterruptException) {
        running = false
      } catch (e: EndOfFileException) {
        return
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  private fun buildNewGameCommands(): ArgumentCompleter {
    val opts = listOf(
      OptDesc("-w", "--width", "Width", StringsCompleter("5")),
      OptDesc("-h", "--height", "Height", StringsCompleter("5")),
      OptDesc("-p", "--players", "Players", StringsCompleter("2", "3")),
      OptDesc(
        "-b",
        "--builders",
        "Builders per player",
        StringsCompleter("2")
      ),
    )

    return ArgumentCompleter(
      StringsCompleter("new"),
      *List(opts.size) { index ->
        OptionCompleter(
          opts,
          index + 1
        )
      }.toTypedArray(),
      NullCompleter.INSTANCE
    )
  }

  private fun node(completer: Completer) = Node(
    completer,
    emptyList()
  )

  private fun node(completer: Completer, vararg more: Node) = Node(
    completer,
    more.toList()
  )

  companion object {
    fun new(): Application {
      val terminal = TerminalBuilder.builder()
        .nativeSignals(true)
        .build()

      return Application(terminal)
    }
  }
}


