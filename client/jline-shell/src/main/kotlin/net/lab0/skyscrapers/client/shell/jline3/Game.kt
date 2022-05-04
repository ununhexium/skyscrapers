package net.lab0.skyscrapers.client.shell.jline3

import org.jline.builtins.ConfigurationPath
import org.jline.builtins.SyntaxHighlighter
import org.jline.builtins.SyntaxHighlighter.DEFAULT_NANORC_FILE
import org.jline.console.CmdLine
import org.jline.console.CommandInput
import org.jline.console.CommandMethods
import org.jline.console.CommandRegistry
import org.jline.console.Printer
import org.jline.console.impl.DefaultPrinter
import org.jline.console.impl.JlineCommandRegistry
import org.jline.console.impl.SystemHighlighter
import org.jline.console.impl.SystemRegistryImpl
import org.jline.keymap.KeyMap
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.ParsedLine
import org.jline.reader.Parser
import org.jline.reader.Reference
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.Size
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.utils.OSUtils
import org.jline.widget.AutosuggestionWidgets
import org.jline.widget.TailTipWidgets
import org.jline.widget.Widgets
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.function.Supplier
import kotlin.io.path.absolute


/**
 * Demo how to create REPL app with JLine.
 *
 * @author [Matti Rinta-Nikkola](mailto:matti.rintanikkola@gmail.com)
 */
object Game {
  @JvmStatic
  fun main(args: Array<String>) {
    try {
      val workDir = Supplier { Paths.get(System.getProperty("user.dir")) }
      //
      // Parser & Terminal
      //
      val parser = DefaultParser()
      parser.setEofOnUnclosedBracket(
        DefaultParser.Bracket.CURLY,
        DefaultParser.Bracket.ROUND,
        DefaultParser.Bracket.SQUARE
      )
      parser.isEofOnUnclosedQuote = true
      parser.escapeChars = null
      parser.setRegexCommand("[:]{0,1}[a-zA-Z!]{1,}\\S*") // change default regex to support shell commands
      parser.blockCommentDelims(DefaultParser.BlockCommentDelims("/*", "*/"))
        .lineCommentDelims(arrayOf("//"));
      val terminal = TerminalBuilder.builder().build()
      if (terminal.width == 0 || terminal.height == 0) {
        terminal.size =
          Size(120, 40) // hard coded terminal size when redirecting
      }
      val executeThread = Thread.currentThread()
      terminal.handle(Terminal.Signal.INT) { signal: Terminal.Signal? -> executeThread.interrupt() }
      //
      // Create jnanorc config file for demo
      //
      val configFolder = Paths.get(System.getProperty("user.home"))
        .resolve(".config")
        .resolve("skyscrapers")
      Files.createDirectory(configFolder)
      val root = configFolder.toAbsolutePath().toString()
      val jnanorcFile = Paths.get(root, DEFAULT_NANORC_FILE).toFile();
      if (!jnanorcFile.exists()) {
        FileWriter(jnanorcFile).use { fw ->
          fw.write("theme " + root + "nanorc/dark.nanorctheme\n");
          fw.write("include " + root + "nanorc/*.nanorc\n");
        }
      }

      //
      // ScriptEngine and command registries
      //
      val configPath = ConfigurationPath(Paths.get(root), Paths.get(root))
      val printer: Printer = DefaultPrinter(configPath)
      // TODO: add builtins?
      // Builtins builtins = new Builtins(workDir, configPath, (String fun) -> new ConsoleEngine.WidgetCreator(consoleEngine, fun));
      val myCommands = MyCommands()
      val systemRegistry =
        ReplSystemRegistry(parser, terminal, workDir, configPath)
      systemRegistry.setCommandRegistries(myCommands)
      // TODO: add my commands completer and description
//            systemRegistry.addCompleter(scriptEngine.getScriptCompleter());
//            systemRegistry.setScriptDescription(scriptEngine::scriptDescription);

      //
      // Command line highlighter
      // TODO: add highligter
      //
      val jnanorc = configPath.getConfig(DEFAULT_NANORC_FILE)
      val commandHighlighter = SyntaxHighlighter.build(jnanorc, "COMMAND")
      val argsHighlighter = SyntaxHighlighter.build(jnanorc, "ARGS")
      val groovyHighlighter = SyntaxHighlighter.build(jnanorc, "Groovy")
      val highlighter = SystemHighlighter(
        commandHighlighter,
        argsHighlighter,
        groovyHighlighter
      )
      if (!OSUtils.IS_WINDOWS) {
        highlighter.setSpecificHighlighter(
          "!",
          SyntaxHighlighter.build(jnanorc, "SH-REPL")
        )
      }
      highlighter.addFileHighlight("nano", "less", "slurp")
      highlighter.addExternalHighlighterRefresh(printer::refresh)
      // TODO: add game highlighter
//            highlighter.addExternalHighlighterRefresh(scriptEngine::refresh);

      //
      // LineReader
      //
      val reader = LineReaderBuilder.builder()
        .terminal(terminal)
        .completer(systemRegistry.completer())
        .parser(parser)
        .highlighter(highlighter)
        .variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ")
        .variable(LineReader.INDENTATION, 2)
        .variable(LineReader.LIST_MAX, 100)
        .variable(LineReader.HISTORY_FILE, Paths.get(root, "history"))
        .option(LineReader.Option.INSERT_BRACKET, true)
        .option(LineReader.Option.EMPTY_WORD_OPTIONS, false)
        .option(
          LineReader.Option.USE_FORWARD_SLASH,
          true
        ) // use forward slash in directory separator
        .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
        .build()
      val autosuggestionWidgets = AutosuggestionWidgets(reader)
      autosuggestionWidgets.enable()
      if (OSUtils.IS_WINDOWS) {
        reader.setVariable(
          LineReader.BLINK_MATCHING_PAREN,
          0
        ) // if enabled cursor remains in begin parenthesis (gitbash)
      }
      //
      // complete command registries
      //
      myCommands.setLineReader(reader)

      //
      // widgets and console initialization
      //
      TailTipWidgets(
        reader,
        { line: CmdLine? -> systemRegistry.commandDescription(line) },
        5,
        TailTipWidgets.TipType.COMPLETER
      )
      val keyMap = reader.keyMaps["main"]!!
      keyMap.bind(Reference(Widgets.TAILTIP_TOGGLE), KeyMap.alt("s"))
      systemRegistry.initialize(Paths.get(root, "init.jline").toFile())
      //
      // REPL-loop
      //
      println(terminal.name + ": " + terminal.type)
      while (true) {
        try {
          systemRegistry.cleanUp() // delete temporary variables and reset output streams
          var line = reader.readLine("game> ")
          line = if (parser
              .getCommand(line)
              .startsWith("!")
          ) line.replaceFirst("!".toRegex(), "! ") else line
          val result = systemRegistry.execute(line)
          // TODO: implement game as an console engine?
//                    consoleEngine.println(result);
        } catch (e: UserInterruptException) {
          // Ignore
        } catch (e: EndOfFileException) {
          break
        } catch (e: Exception) {
          systemRegistry.trace(e) // print exception and save it to console variable
        } catch (e: Error) {
          systemRegistry.trace(e)
        }
      }
      systemRegistry.close() // persist pipeline completer names etc
    } catch (t: Throwable) {
      t.printStackTrace()
    }
  }

  class MyCommands : JlineCommandRegistry(), CommandRegistry {
    private var reader: LineReader? = null

    init {
      val commandExecute: MutableMap<String, CommandMethods> = HashMap()
      commandExecute["new"] =
        CommandMethods({ input: CommandInput -> newGame(input) }) { command: String? ->
          newGameCompleter(command)
        }
      registerCommands(commandExecute)
    }

    override fun commandInfo(command: String): List<String> {
      return Arrays.asList(
        "Starts a new game"
      )
    }

    fun setLineReader(reader: LineReader?) {
      this.reader = reader
    }

    private fun terminal(): Terminal {
      return reader!!.terminal
    }

    private fun newGame(input: CommandInput) {
      val usage = arrayOf(
        "new - new game"
      )
      try {
        terminal().writer().println("Hello game")
      } catch (e: Exception) {
        saveException(e)
      }
    }

    internal inner class NewGameCompleter : Completer {
      private fun newGame(input: CommandInput) {
        val usage = arrayOf(
          "new - new game"
        )
        try {
          terminal().writer().println("Hello game")
        } catch (e: Exception) {
          saveException(e)
        }
      }

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

    fun newGameCompleter(command: String?): List<Completer?> {
      return Arrays.asList<Completer?>(NewGameCompleter())
    }
  }

  private class ReplSystemRegistry(
    parser: Parser?,
    terminal: Terminal?,
    workDir: Supplier<Path>?,
    configPath: ConfigurationPath?
  ) : SystemRegistryImpl(parser, terminal, workDir, configPath) {
    override fun isCommandOrScript(command: String): Boolean {
      return command.startsWith("!") || super.isCommandOrScript(command)
    }
  }
}