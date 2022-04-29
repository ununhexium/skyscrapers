package net.lab0.skyscrapers.state

import net.lab0.skyscrapers.flip

data class GameStateData(
  override val buildings: List<List<Int>>,
  override val seals: List<List<Boolean>>,
  override val builders: List<List<Int?>>,
) : GameState {
  companion object {
    fun from(
      buildings: String,
      seals: String,
      builders: String
    ): GameStateData {
      val buildingsData = buildings
        .filter { it.isDigit() || it == '\n' }
        .split("\n")
        .map { it.map { char -> char.toString().toInt() } }
        .flip()

      val sealsData = seals
        .filter { it.isDigit() || it == '\n' }
        .split("\n")
        .map { it.map { char -> char == '1' } }
        .flip()

      val playersData = builders
        .filter { it.isDigit() || it == '\n' || it == '.' }
        .split("\n")
        .map {
          it.map { char ->
            if (char == '.') null else char.toString().toInt()
          }
        }
        .flip()

      return GameStateData(buildingsData, sealsData, playersData)
    }
  }

  override fun toString(): String {
    val buildings = buildings.flip().joinToString(separator = "\n") {
      it.joinToString(separator = " ")
    }

    val seals = seals.flip().joinToString(separator = "\n") {
      it.joinToString(separator = " ") { present -> if (present) "1" else "0" }
    }

    val builders = builders.flip().joinToString(separator = "\n") {
      it.joinToString(separator = " ") { player ->
        player?.toString() ?: "."
      }
    }

    return """
      |Buildings
      |$buildings
      |
      |Seals
      |$seals
      |
      |Builders
      |$builders
    """.trimMargin()
  }
}
