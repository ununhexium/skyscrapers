package net.lab0.skyscrapers

import net.lab0.skyscrapers.api.BlocksData
import net.lab0.skyscrapers.structure.Height

object Defaults {
  const val WIDTH = 5
  const val HEIGHT = 5
  const val PLAYER_COUNT = 2

  const val BUILDERS_PER_PLAYER = 2

  val BLOCKS = BlocksData(
    mapOf(
      Height(0) to 17,
      Height(1) to 21,
      Height(2) to 19,
      Height(3) to 14,
    )
  )
}
