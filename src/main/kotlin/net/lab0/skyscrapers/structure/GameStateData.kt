package net.lab0.skyscrapers.structure

import net.lab0.skyscrapers.api.GameState

data class GameStateData(
  override val phase: Phase,
  override val blocks: Map<Height, Int>,
  override val currentPlayer: Int,
  override val buildings: Matrix<Int>,
  override val seals: Matrix<Boolean>,
  override val builders: Matrix<Int?>,
) : GameState {
  companion object {
    fun from(
      phase: Phase? = null,
      blocks: Map<Height, Int>,
      buildings: String,
      seals: String,
      builders: String,
    ): GameStateData {
      // TODO check that all the matrices has the same size
      val buildingsData = Matrix.from(buildings) { it.toInt() }

      val sealsData = Matrix.from(seals) { it == "1" }

      val playersData = Matrix.from(builders) {
        if (it == ".") null else it.toInt()
      }

      // guess the phase

      val actualPhase = phase
        ?: if (buildingsData.data.sumOf { it.sumOf { it } } > 0) Phase.MOVEMENT else Phase.PLACEMENT

      // TODO: guess current player
      // TODO: set blocks

      return GameStateData(actualPhase, blocks,0, buildingsData, sealsData, playersData)
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
