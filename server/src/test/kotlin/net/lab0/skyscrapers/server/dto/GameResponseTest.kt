package net.lab0.skyscrapers.server.dto

import net.lab0.skyscrapers.logic.Player
import net.lab0.skyscrapers.logic.api.BlocksData
import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.structure.Bounds
import net.lab0.skyscrapers.logic.structure.Height
import net.lab0.skyscrapers.logic.structure.Matrix
import net.lab0.skyscrapers.logic.structure.Phase
import net.lab0.skyscrapers.server.GameResponse
import net.lab0.skyscrapers.server.value.GameName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GameResponseTest {

  @Test
  fun `quick check conversion to DTO`() {
    val width = 5
    val height = 4
    val buildersPerPlayer = 3
    val blocks = mapOf(
      Height(0) to 31,
      Height(1) to 22,
      Height(2) to 13,
    )

    val buildings = listOf(
      listOf(1, 2, 0, 0, 0).map { Height(it) },
      listOf(0, 3, 0, 0, 0).map { Height(it) },
      listOf(0, 4, 5, 6, 7).map { Height(it) },
      listOf(0, 0, 0, 0, 8).map { Height(it) },
    )

    val seals = listOf(
      listOf(true, false, true, false, true),
      listOf(false, true, false, true, false),
      listOf(true, false, true, false, true),
      listOf(false, true, false, true, false),
    )

    val builders = listOf(
      listOf(0, 1, 0, 1, 0),
      listOf(1, null, null, null, null),
      listOf(null, null, null, null, null),
      listOf(null, null, null, null, null),
    )

    val state = GameState(
      Bounds(width, height),
      listOf(Player(0, false), Player(1, true)),
      buildersPerPlayer,
      BlocksData(blocks),
      Matrix(buildings),
      Matrix(seals),
      Matrix(builders),
    )

    val dto = GameResponse(GameName("name"), state)

    val serializedBounds = BoundsDTO(width, height)
    val serializedPlayers = listOf(
      PlayerDTO(0, false),
      PlayerDTO(1, true),
    )
    val serializedBlocksData = BlocksDataDTO(
      blocks.mapKeys { it.value }
    )
    val serializedBuildings =
      MatrixDTO(buildings.map { it.map { it.value } })
    val serializedSeals = MatrixDTO(seals)
    val serializedBuilders = MatrixDTO(builders)

    assertThat(dto.state.bounds).isEqualTo(serializedBounds)
    assertThat(dto.state.players).isEqualTo(serializedPlayers)
    assertThat(dto.state.maxBuildersPerPlayer).isEqualTo(buildersPerPlayer)
    assertThat(dto.state.buildings).isEqualTo(serializedBuildings)
    assertThat(dto.state.seals).isEqualTo(serializedSeals)
    assertThat(dto.state.builders).isEqualTo(serializedBuilders)

    assertThat(dto).isEqualTo(
      GameResponse(
        "name",
        StateDTO(
          bounds = serializedBounds,
          players = serializedPlayers,
          maxBuildersPerPlayer = buildersPerPlayer,
          currentPlayer = 0,
          blocks = serializedBlocksData,
          buildings = serializedBuildings,
          seals = serializedSeals,
          builders = serializedBuilders,
          phase = PhaseDTO.FINISHED, // Because only 1 active player
        ),
      )
    )
  }
}