package net.lab0.skyscrapers.client.shell.spring.component

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.shell.spring.data.Cell
import net.lab0.skyscrapers.client.shell.spring.data.CellMetadata
import net.lab0.skyscrapers.client.shell.spring.data.HierarchyResult.StateUpdate
import org.jline.utils.AttributedString
import org.springframework.shell.result.TerminalAwareResultHandler
import org.springframework.shell.table.ArrayTableModel
import org.springframework.shell.table.TableBuilder
import org.springframework.stereotype.Component

@Component
class StateUpdateHandler(val cellPainter: CellPainter) :
  TerminalAwareResultHandler<StateUpdate>() {

  override fun doHandleResult(update: StateUpdate) {
    terminal.writer().println(
      TableBuilder(ArrayTableModel(transform(update))).build()
    )
  }

  internal fun transform(update: StateUpdate): Array<Array<AttributedString>> {
    val state = update.state
    val meta = CellMetadata(state.blocks.maxHeight(), state.players.size)

    val array = state.bounds.abscissaRange.map { x ->
      state.bounds.ordinateRange.map { y ->
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
