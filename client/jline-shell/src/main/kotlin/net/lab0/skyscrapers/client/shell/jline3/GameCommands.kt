package net.lab0.skyscrapers.client.shell.jline3

import org.jline.console.CommandInput
import org.jline.console.CommandMethods
import org.jline.console.CommandRegistry
import org.jline.console.impl.JlineCommandRegistry
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine
import org.jline.terminal.Terminal
import java.util.*

class GameCommands : JlineCommandRegistry(), CommandRegistry {
  lateinit var reader: LineReader

  private val commandExecute = mutableMapOf<String, CommandMethods>()

  init {
    commandExecute["foo"] = CommandMethods(::newGame, ::newGameCompleter)
    registerCommands(commandExecute)
  }

  override fun commandInfo(command: String): List<String> {
    return listOf("Starts a new game")
  }

  private fun terminal(): Terminal {
    return reader.terminal
  }

  private fun newGame(input: CommandInput) {
    try {
      terminal().writer().println("Hello game")
    } catch (e: Exception) {
      saveException(e)
    }
  }

  internal inner class NewGameCompleter : Completer {
    override fun complete(
      reader: LineReader,
      line: ParsedLine,
      candidates: MutableList<Candidate>
    ) {
      val params = Arrays.asList(
        "--width",
        "--height",
        "--players",
        "--builders"
      )
      candidates.addAll(
        params.map { value: String? -> Candidate(value) }
      )
    }
  }

  private fun newGameCompleter(command: String?) =
    listOf(NewGameCompleter())
}
