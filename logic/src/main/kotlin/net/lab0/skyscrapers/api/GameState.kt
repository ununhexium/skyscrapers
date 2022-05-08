package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.Player
import net.lab0.skyscrapers.structure.CompositePosition
import net.lab0.skyscrapers.structure.Dimension
import net.lab0.skyscrapers.structure.Height
import net.lab0.skyscrapers.structure.Matrix
import net.lab0.skyscrapers.structure.Phase
import net.lab0.skyscrapers.structure.Position

data class GameState(
  val dimentions: Dimension,
  val players: List<Player>,
  val maxBuildersPerPlayer: Int,
  val blocks: BlocksData,
  val buildings: Matrix<Height>,
  val seals: Matrix<Boolean>,
  val builders: Matrix<Int?>,
) {
  init {
    if (buildings.dimensions != dimentions || seals.dimensions != dimentions || buildings.dimensions != dimentions)
      throw IllegalStateException("All the matrices must have the same size")
  }

  companion object {
    fun from(
      players: List<Player>,
      buildersPerPlayer: Int,
      blocks: BlocksData,
      buildings: String,
      seals: String,
      builders: String,
    ): GameState {
      val buildingsData = Matrix.from(buildings) { it.toInt() }

      val sealsData = Matrix.from(seals) { it == "1" }

      val playersData = Matrix.from(builders) {
        if (it == ".") null else it.toInt()
      }

      return GameState(
        Dimension(buildingsData.columns, buildingsData.rows),
        players,
        buildersPerPlayer,
        blocks,
        buildingsData.map { Height(it) },
        sealsData,
        playersData
      )
    }

    /**
     * Parses board displays like
     *
     * ```
     * Board
     *   0 (0)   1   0   0
     * (3) (1) (1) (0)   0
     *   0  B0  A0 (0) (1)
     *  A0 (1)   0 (0)  B1
     * (0) (0) (0) (1) (0)
     * Blocks: 0:3, 1:13, 2:18, 3:13
     * Players: 0:a, 1:d
     * ```
     */
    fun from(input: String): GameState {
      val lines = input.split(Regex("\n"))

      if (lines.first() != "Board")
        throw IllegalArgumentException("The first line must be 'Board'")

      // parse board
      val positions =
        lines.drop(1).takeWhile { !it.startsWith("Blocks:") }.map { line ->
          line.trim().split(Regex(" +")).map { position ->
            CompositePosition.from(position)
          }
        }

      val posMatrix = Matrix(positions)

      val buildings = posMatrix.map { Height(it.building) }
      val seals = posMatrix.map { it.seal }
      val builders = posMatrix.map { it.builder }

      // parse blocks
      val blocksPrefix = "Blocks: "
      val blocks =
        lines
          .dropWhile { !it.startsWith(blocksPrefix) }
          .take(1)
          .first()
          .substring(blocksPrefix.length)
          .split(", ")
          .map { it.split(":") }
          .map { it.component1().toInt() to it.component2() }
          .associate { Height(it.first) to it.second.toInt() }

      // parse players
      val playersPrefix = "Players: "

      val players =
        lines
          .dropWhile { !it.startsWith(playersPrefix) }
          .take(1)
          .first()
          .substring(playersPrefix.length)
          .split(", ")
          .map { it.split(":") }
          .map { it.component1().toInt() to (it.component2() == "a") }
          .map { Player(it.first, it.second) }

      return GameState(
        buildings.dimensions,
        players,
        builders.data.flatten().filterNotNull().groupBy { it }.entries.first().value.size,
        BlocksData(blocks),
        buildings,
        seals,
        builders
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

  fun toCompositeString(): String {
    val fullState =
      buildings.merge(seals.merge(builders) { it }) {
        CompositePosition(
          it.first.value,
          it.second.first,
          it.second.second
        )
      }

    val stringyfied = fullState.map {
      val builder = it.builder

      if (it.seal) "(${it.building})"
      else if (builder != null) ('A' + builder).toString() + it.building
      else it.building.toString()
    }.map { it.padStart(3, ' ') }

    return """
      |Board
      |$stringyfied
      |Blocks: ${blocks.toShortString()}
      |Players: ${
      players.joinToString(separator = ", ") {
        "${it.id}:${if (it.active) "a" else "d"}"
      }
    }
    """.trimMargin()
  }

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
    get() =
      if (isFinished()) {
        Phase.FINISHED
      } else {
        val totalBuilders = players.size * maxBuildersPerPlayer
        val placedBuilders = builders.count { it != null }
        if (placedBuilders < totalBuilders) Phase.PLACEMENT else Phase.MOVEMENT
      }

  fun isFinished() =
    players.count { it.active } == 1 // or reached max height

  // TODO: encapsulate these in a backdoor?

  fun isWithinBounds(pos: Position) =
    pos.inBounds(0, buildings.lastColumn, 0, buildings.lastRow)

  fun height(pos: Position, height: Int) =
    copy(buildings = buildings.copyAndSet(pos, Height(height)))

  fun seal(pos: Position) =
    copy(seals = seals.copyAndSet(pos, true))

  fun placeBuilder(player: Int, position: Position) =
    copy(builders = builders.copyAndSet(position, player))

  fun move(turn: Move) =
    copy(builders = builders.copyAndSwap(turn.start, turn.target))
}
