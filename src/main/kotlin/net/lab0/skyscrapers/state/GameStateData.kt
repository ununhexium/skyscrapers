package net.lab0.skyscrapers.state

import net.lab0.skyscrapers.flip

data class GameStateData(
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
}
