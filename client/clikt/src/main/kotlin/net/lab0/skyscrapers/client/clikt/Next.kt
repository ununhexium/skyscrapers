package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import net.lab0.skyscrapers.engine.api.Series

class Next(private val series: Series) : CliktCommand(name = "next") {
  override fun run() {
    series.start()
  }
}
