package net.lab0.skyscrapers.client.shell.spring.data

import net.lab0.skyscrapers.api.structure.Height

data class CellMetadata(val maxHeight: Height, val playersCount: Int) {
  init {
    require(maxHeight.value > 0) {
      "The maximum height must be strictly positive."
    }
    require(playersCount > 0) {
      "The players count must be strictly positive."
    }
  }
}
