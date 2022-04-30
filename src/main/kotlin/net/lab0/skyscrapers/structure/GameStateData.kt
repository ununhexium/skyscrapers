package net.lab0.skyscrapers.structure

import net.lab0.skyscrapers.api.GameState

data class GameStateData(
  // TODO make more typesafe with value classes
  override val buildings: Matrix<Int>,
  override val seals: Matrix<Boolean>,
  override val builders: Matrix<Int?>,
) : GameState {
  companion object {
    fun from(
      buildings: String,
      seals: String,
      builders: String
    ): GameStateData {
      // TODO check that all the matrices has the same size
      val buildingsData = Matrix.from(buildings) { it.toInt() }

      val sealsData = Matrix.from(seals) { it == "1" }

      val playersData = Matrix.from(builders) {
        if (it == ".") null else it.toInt()
      }

      return GameStateData(buildingsData, sealsData, playersData)
    }
  }

  override fun toString(): String {

    return """
      |Buildings
      |$buildings
      |
      |Seals
      |${seals.toString { if (it) "1" else "0" }}
      |
      |Builders
      |${builders.toString { it?.toString() ?: "." }}
    """.trimMargin()
  }

  override fun isWithinBounds(pos: Position) =
    pos.inBounds(0, buildings.columns, 0, buildings.rows)
}
