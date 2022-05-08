package net.lab0.skyscrapers.client.shell.jline3

import net.lab0.skyscrapers.Defaults
import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.structure.Phase
import org.jline.builtins.Completers
import org.jline.builtins.Completers.TreeCompleter
import org.jline.builtins.Completers.TreeCompleter.node
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine
import org.jline.reader.impl.completer.AggregateCompleter
import org.jline.reader.impl.completer.ArgumentCompleter
import org.jline.reader.impl.completer.NullCompleter
import org.jline.reader.impl.completer.StringsCompleter
import java.util.concurrent.atomic.AtomicReference

class GameCompleter(val ref: AtomicReference<Game?>) : Completer {
  override fun complete(
    reader: LineReader,
    line: ParsedLine,
    candidates: MutableList<Candidate>
  ) {
    val game = ref.get()

    val completer = AggregateCompleter(
      StringsCompleter("show"),
      if (game == null) {
        buildNewGameCommands()
      } else {
        if (game.state.phase == Phase.PLACEMENT) {
          buildPlacementCommands(game)
        } else {
          NullCompleter.INSTANCE
        }
      }
    )

    completer.complete(reader, line, candidates)
  }

  private fun buildPlacementCommands(game: Game): Completer {
    return TreeCompleter(
      node(
        "place-builder",
        node("randomly"),
        node(
          "at",
          node(
            "-x",
            node(
              IntRangeCompleter(0 until game.state.dimentions.width),
              node(
                "-y",
                node(IntRangeCompleter(0 until game.state.dimentions.height))
              )
            ),
          )
        )
      )
    )
  }

  private fun buildNewGameCommands(): Completer {
    val opts = listOf(
      Completers.OptDesc(
        "-w",
        "--width",
        "Width",
        StringsCompleter(Defaults.WIDTH.toString())
      ),
      Completers.OptDesc(
        "-h",
        "--height",
        "Height",
        StringsCompleter(Defaults.HEIGHT.toString())
      ),
      Completers.OptDesc(
        "-p",
        "--players",
        "Players",
        StringsCompleter(Defaults.PLAYER_COUNT.toString())
      ),
      Completers.OptDesc(
        "-b",
        "--builders",
        "Builders per player",
        StringsCompleter(Defaults.BUILDERS_PER_PLAYER.toString())
      ),
    )

    return ArgumentCompleter(
      StringsCompleter("new"),
      *List(opts.size) {
        Completers.OptionCompleter(
          opts,
          it + 1
        )
      }.toTypedArray(),
      NullCompleter.INSTANCE
    )
  }

}