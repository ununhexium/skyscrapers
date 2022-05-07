package net.lab0.skyscrapers.client.shell.jline3

import net.lab0.skyscrapers.Defaults
import net.lab0.skyscrapers.api.Game
import org.jline.builtins.Completers.OptDesc
import org.jline.builtins.Completers.OptionCompleter
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReader.Option
import org.jline.reader.LineReaderBuilder
import org.jline.reader.ParsedLine
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.reader.impl.completer.ArgumentCompleter
import org.jline.reader.impl.completer.NullCompleter
import org.jline.reader.impl.completer.StringsCompleter
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

        println(words)
        when (words.first()) {
          "new" -> ref.set(Game.new())
        }

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

  companion object {
    fun new(): Application {
      return Application()
    }
  }
}


