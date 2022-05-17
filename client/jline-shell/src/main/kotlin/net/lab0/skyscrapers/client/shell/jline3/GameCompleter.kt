package net.lab0.skyscrapers.client.shell.jline3

import net.lab0.skyscrapers.engine.Defaults
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.structure.Phase
import net.lab0.skyscrapers.engine.utils.StateBrowser
import org.jline.builtins.Completers
import org.jline.builtins.Completers.TreeCompleter
import org.jline.builtins.Completers.TreeCompleter.Node
import org.jline.builtins.Completers.TreeCompleter.node as treeNode
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
        when (game.state.phase) {
          Phase.PLACEMENT -> buildPlacementCommands(game)
          Phase.MOVEMENT -> buildMovementCommands(game)
          else -> NullCompleter.INSTANCE
        }
      }
    )

    completer.complete(reader, line, candidates)
  }

  private fun buildMovementCommands(game: Game): Completer {
    val state = game.state
    val browser = StateBrowser(state, game.ruleBook)

    return TreeCompleter(
      node(
        "move-builder",
        listOf(
          node(
            "--from",
            browser.getMovableBuilders(state.currentPlayer).map { start ->
              node(
                "${start.x},${start.y}",
                listOf(
                  node(
                    "--to",
                    start.getSurroundingPositionsWithin(state.bounds)
                      .filter{ target ->
                        browser.builderCanMoveTo(start, target)
                      }
                      .map { target ->
                        node(
                          "${target.x},${target.y}",
                            listOf(
                              "--andBuild",
                              "--andSeal",
                              "--andWin",
                            ).map {
                              node(
                                it,
                                target
                                  .getSurroundingPositionsWithin(state.bounds)
                                  .map { buildOrSeal ->
                                    node("${buildOrSeal.x},${buildOrSeal.y}")
                                  }
                              )
                            }
                          )
                      }
                  )
                )
              )
            }
          )
        )
      )
    )
  }

  private fun node(string: String) =
    Node(StringsCompleter(string), listOf())

  private fun node(string: String, more: List<Node>) =
    Node(StringsCompleter(string), more)

  private fun buildPlacementCommands(game: Game): Completer {
    return TreeCompleter(
      treeNode(
        "place-builder",
        treeNode("randomly"),
        treeNode(
          "at",
          treeNode(
            "-x",
            treeNode(
              IntRangeCompleter(0 until game.state.bounds.width),
              treeNode(
                "-y",
                treeNode(IntRangeCompleter(0 until game.state.bounds.height))
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