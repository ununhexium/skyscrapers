package net.lab0.skyscrapers.client.shell.jline3

import net.lab0.skyscrapers.client.clikt.GameCli
import net.lab0.skyscrapers.engine.api.Series
import net.lab0.skyscrapers.engine.exception.GameRuleViolationException
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader.Option
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.TerminalBuilder


fun main(args: Array<String>) {
  Application().run()
}

class Application {

  var running = true

  fun run() {

    val terminal = TerminalBuilder.terminal()

    val parser = DefaultParser()

    val series = Series.newBestOf1()

    val cli = GameCli.new(series, terminal.writer())

    val reader = LineReaderBuilder
      .builder()
      .terminal(terminal)
      .parser(parser)
      .completer(GameCompleter(series))
      .option(Option.AUTO_MENU_LIST, true)
      .build()

    running = true
    while (running) {
      try {
        val line = reader.readLine(
          series
            .getCurrentRound()
            ?.state
            ?.currentPlayer
            ?.let { "player ${'A'.code + it}>" }
            ?: "game>"
        )

        val parsedLine = reader.parser.parse(line, line.lastIndex)

        val words = parsedLine.words()

        cli.parse(words)
      } catch (e: GameRuleViolationException) {
        val writer = terminal.writer()

        val message = " !! Rule violated (you monster!) !! "
        writer.println(message)
        writer.println("-".repeat(message.length))

        e.violation.forEach {
          writer.println(it.name)
          writer.println(it.description)
          writer.println()
          writer.println(it.detail)
        }
      } catch (e: UserInterruptException) {
        // ignore
      } catch (e: EndOfFileException) {
        running = false
      } catch (e: Exception) {
        e.printStackTrace(terminal.writer())
      } catch (t: Throwable) {
        println("Threw ${t.javaClass}")
        throw t
      }
    }
  }

  companion object {
    fun new(): Application {
      return Application()
    }
  }
}


