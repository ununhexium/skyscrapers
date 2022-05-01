package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.Player
import net.lab0.skyscrapers.structure.Height
import net.lab0.skyscrapers.structure.Matrix
import net.lab0.skyscrapers.structure.Phase
import net.lab0.skyscrapers.structure.Position

data class GameState(
  val players: List<Player>,
  val maxBuildersPerPlayer: Int,
  val blocks: BlocksData,
  val buildings: Matrix<Height>,
  val seals: Matrix<Boolean>,
  val builders: Matrix<Int?>,
) {
  companion object {
    fun from(
      players: List<Player>,
      buildersPerPlayer: Int,
      blocks: BlocksData,
      buildings: String,
      seals: String,
      builders: String,
    ): GameState {
      // TODO check that all the matrices has the same size
      val buildingsData = Matrix.from(buildings) { it.toInt() }

      val sealsData = Matrix.from(seals) { it == "1" }

      val playersData = Matrix.from(builders) {
        if (it == ".") null else it.toInt()
      }

      // TODO: guess current player
      // TODO: set blocks

      return GameState(
        players,
        buildersPerPlayer,
        blocks,
        buildingsData.map { Height(it) },
        sealsData,
        playersData
      )
    }
  }

  val currentPlayer: Int
    get() = players.first().id

  override fun toString(): String {

    return """
      |Buildings
      |${buildings.map { it.value }}
      |
      |Seals
      |${seals.toString { if (it) "1" else "0" }}
      |
      |Builders
      |${builders.toString { it?.toString() ?: "." }}
    """.trimMargin()
  }

  fun getHeight(pos: Position): Height =
    buildings[pos]

  fun getBuilders(player: Int): List<Position> {
    val result = mutableListOf<Position?>()

    builders.mapIndexedTo(result) { position, cell ->
      if (cell == player) position else null
    }

    return result.filterNotNull()
  }

  fun hasBuilder(position: Position) =
    builders[position] != null

  val phase: Phase
    get() {
      val totalBuilders = players.size * maxBuildersPerPlayer
      val placedBuilders = builders.count { it != null }
      return if (placedBuilders < totalBuilders) Phase.PLACEMENT else Phase.MOVEMENT
    }

  fun isFinished() =
    players.count { it.active } == 1 // or reached max height

  // TODO: encapsulate these in a backdoor

  fun isWithinBounds(pos: Position) =
    pos.inBounds(0, buildings.columns, 0, buildings.rows)

  fun height(pos: Position, height: Int) =
    copy(buildings = buildings.copyAndSet(pos, Height(height)))

  fun seal(pos: Position) =
    copy(seals = seals.copyAndSet(pos, true))

  fun placeBuilder(player: Int, position: Position) =
    copy(builders = builders.copyAndSet(position, player))
}
