package net.lab0.skyscrapers.client.shell.jline3

import org.jline.builtins.Completers
import org.jline.builtins.Completers.OptDesc
import org.jline.builtins.Completers.OptionCompleter
import org.jline.builtins.Completers.TreeCompleter
import org.jline.builtins.Completers.TreeCompleter.Node
import org.jline.reader.Candidate
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
    val parser = DefaultParser()
    val reader = LineReaderBuilder
      .builder()
      .terminal(terminal)
      .parser(parser)
      .completer(
        AggregateCompleter(
          TreeCompleter(
            Node(
              StringsCompleter("new"),
              listOf(
                Node(
                  StringsCompleter("--width"),
                  listOf(3, 5, 7)
                    .map { it.toString() }
                    .map { Node(StringsCompleter(it), listOf()) }
                ),
                Node(
                  StringsCompleter("--height"),
                  listOf(2, 4, 6)
                    .map { it.toString() }
                    .map { Node(StringsCompleter(it), listOf()) }
                )

              )
            ),
            Node(
              StringsCompleter("restart"),
              listOf()
            )
          ),
          ArgumentCompleter(
            StringsCompleter("new2"),
            OptionCompleter(
              listOf(
                StringsCompleter("--width"),
              ),
              {
                listOf(
                  OptDesc("-w", "--width", "The width for the new game"),
                )
              },
              1
            ),
            OptionCompleter(
              listOf(
                StringsCompleter("--height"),
              ),
              {
                listOf(
                  OptDesc("-h", "--height", "The height for the new game"),
                )
              },
              3
            ),
          )
        )
      )

//          OptionCompleter(
//            listOf(
//              Completers.OptDesc(
//                "-w",
//                "--width",
//                "Width",
//
//              ),
//              Completers.OptDesc(
//                "-h",
//                "--height",
//                "Height",
//                Completer { reader, line, candidates ->
//                  candidates.addAll(
//                    listOf(
//                      3, 4, 5
//                    ).map { Candidate(it.toString()) })
//                }),
//            ),
//            0
//          )
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

  companion object {
    fun new(): Application {
      val terminal = TerminalBuilder.builder()
        .nativeSignals(true)
        .build()

      return Application(terminal)
    }
  }
}


