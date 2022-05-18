package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import net.lab0.skyscrapers.engine.action.DSL
import net.lab0.skyscrapers.engine.api.Series

class PlaceAt(val series: Series) : CliktCommand(name = "at") {
  private val x by option("-x", "--x-position").int().required()
  private val y by option("-y", "--y-position").int().required()

  override fun run() {
    series.withGame {
      play(
        DSL.player(state.currentPlayer).placement.addBuilder(x, y)
      )
    }
  }
}
