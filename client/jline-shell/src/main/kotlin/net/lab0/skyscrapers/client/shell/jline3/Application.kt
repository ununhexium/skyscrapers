package net.lab0.skyscrapers.client.shell.jline3

import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.client.shell.jline3.execution.GameCli
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader.Option
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.TerminalBuilder
import org.jline.widget.AutosuggestionWidgets
import java.util.concurrent.atomic.AtomicReference


fun main(args: Array<String>) {
  Application().run()
}

class Application {

  var running = true

  fun run() {

    val terminal = TerminalBuilder.terminal()

    val parser = DefaultParser()

    val ref = AtomicReference<Game?>(null)

    val cli = GameCli.new(ref, terminal.writer())

    val reader = LineReaderBuilder
      .builder()
      .terminal(terminal)
      .parser(parser)
      .completer(GameCompleter(ref))
      .option(Option.AUTO_MENU_LIST, true)
      .build()

    val autosuggestionWidgets = AutosuggestionWidgets(reader)
    autosuggestionWidgets.enable()

    running = true
    while (running) {
      try {
        val line = reader.readLine("prompt > ")

        val parsedLine = reader.parser.parse(line, line.lastIndex)

        val words = parsedLine.words()

        cli.main(words)
      } catch (e: UserInterruptException) {
        running = false
      } catch (e: EndOfFileException) {
        return
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  companion object {
    fun new(): Application {
      return Application()
    }
  }
}


