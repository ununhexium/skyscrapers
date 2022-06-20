package net.lab0.skyscrapers.client.shell.spring.component.result

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.shell.spring.background
import net.lab0.skyscrapers.client.shell.spring.component.CellPainterComponent
import net.lab0.skyscrapers.client.shell.spring.component.DefaultPaletteComponent
import net.lab0.skyscrapers.client.shell.spring.data.Cell
import net.lab0.skyscrapers.client.shell.spring.data.CellMetadata
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult.Ok.StateUpdate
import net.lab0.skyscrapers.client.shell.spring.foreground
import net.lab0.skyscrapers.client.shell.spring.plus
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.result.TerminalAwareResultHandler
import org.springframework.stereotype.Component

@Component
class ShellResultHandler(
  val cellPainter: CellPainterComponent,
  val palette: DefaultPaletteComponent,
) : TerminalAwareResultHandler<ShellResult>() {

  private val INDENT = "    "

  override fun doHandleResult(result: ShellResult) {
    handle(result)
  }

  fun handle(result: ShellResult, indent: Int = 0) {
    when (result) {
      is StateUpdate -> update(indent, result)
      is ShellResult.Ok.Text -> indented(indent, result.output)
      is ShellResult.Problem.Client -> TODO("Implement Client problem display")
      is ShellResult.Problem.Text -> printError(indent, result.output)
      is ShellResult.Tree -> {
        handle(result.node, indent)
        result.children.forEach {
          handle(it, indent = indent + 1)
        }
      }
    }
  }

  private fun printError(indent: Int, s: String) =
    indented(
      indent,
      AttributedString(
        s,
        AttributedStyle.DEFAULT.foreground(palette.errorForeground)
      ).toAnsi(terminal)
    )

  private fun update(indent: Int, update: StateUpdate) {
    val emptyString = AttributedString(
      "",
      AttributedStyle.DEFAULT.background(palette.backgroundColour)
    )

    val singleSpace = AttributedString(
      " ",
      AttributedStyle.DEFAULT.background(palette.backgroundColour)
    )

    val doubleSpace = AttributedString(
      "  ",
      AttributedStyle.DEFAULT.background(palette.backgroundColour)
    )

    transform(update).map { row ->
      row.map { cell ->
        when (cell.length) {
          1 -> doubleSpace + cell + singleSpace
          2 -> singleSpace + cell + singleSpace
          else -> throw IllegalStateException("Only expect elements of length 1 or 2")
        }
      }.fold(emptyString) { acc, e ->
        acc + e
      }
    }.forEach {
      indented(indent, it.toAnsi(terminal))
    }

    terminal.writer().println(update.comment)
  }

  private fun indented(indent: Int, string: String) {
    repeat(indent) { terminal.writer().print(INDENT) }
    terminal.writer().println(string)
  }

  internal fun transform(update: StateUpdate): Array<Array<AttributedString>> {
    val state = update.state
    val meta = CellMetadata(state.blocks.maxHeight(), state.players.size)

    val array = state.bounds.ordinateRange.map { y ->
      state.bounds.abscissaRange.map { x ->
        val position = Position(x, y)
        cellPainter.colorize(
          Cell(
            state.buildings[position],
            state.seals[position],
            state.builders[position],
          ),
          meta
        )
      }.toTypedArray()
    }.toTypedArray()

    return array
  }
}
