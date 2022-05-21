package net.lab0.skyscrapers.server.dto

import net.lab0.skyscrapers.engine.structure.Position
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class TurnTypeDTOTest {
  @Test
  fun `can deserialize a give up turn`() {
    Assertions.assertThat(
      net.lab0.skyscrapers.server.dto.TurnTypeDTO.Companion
        .giveUp(giveUp = net.lab0.skyscrapers.server.dto.GiveUpTurnDTO(116))
        .toTurnType()
    ).isEqualTo(
      net.lab0.skyscrapers.engine.api.TurnType.GiveUpTurn(116)
    )
  }

  @Test
  fun `can deserialize a place turn`() {
    Assertions.assertThat(
      net.lab0.skyscrapers.server.dto.TurnTypeDTO.Companion
        .place(
          place = net.lab0.skyscrapers.server.dto.PlaceTurnDTO(
            1,
            net.lab0.skyscrapers.server.dto.PositionDTO(2, 3)
          )
        )
        .toTurnType()
    ).isEqualTo(
      net.lab0.skyscrapers.engine.api.TurnType.PlacementTurn(1, Position(2, 3))
    )
  }

  @Test
  fun `can deserialize a build turn`() {
    Assertions.assertThat(
      net.lab0.skyscrapers.server.dto.TurnTypeDTO.Companion.build(
        build = net.lab0.skyscrapers.server.dto.BuildTurnDTO(
          1,
          net.lab0.skyscrapers.server.dto.PositionDTO(2, 3),
          net.lab0.skyscrapers.server.dto.PositionDTO(4, 5),
          net.lab0.skyscrapers.server.dto.PositionDTO(6, 7),
        )
      ).toTurnType()
    ).isEqualTo(
      net.lab0.skyscrapers.engine.api.TurnType.MoveTurn.BuildTurn(
        1,
        Position(2, 3),
        Position(4, 5),
        Position(6, 7)
      )
    )
  }

  @Test
  fun `can deserialize a seal turn`() {
    Assertions.assertThat(
      net.lab0.skyscrapers.server.dto.TurnTypeDTO.Companion.seal(
        seal = net.lab0.skyscrapers.server.dto.SealTurnDTO(
          1,
          net.lab0.skyscrapers.server.dto.PositionDTO(2, 3),
          net.lab0.skyscrapers.server.dto.PositionDTO(4, 5),
          net.lab0.skyscrapers.server.dto.PositionDTO(6, 7),
        )
      ).toTurnType()
    ).isEqualTo(
      net.lab0.skyscrapers.engine.api.TurnType.MoveTurn.SealTurn(
        1,
        Position(2, 3),
        Position(4, 5),
        Position(6, 7)
      )
    )
  }

  @Test
  fun `can deserialize a win turn`() {
    Assertions.assertThat(
      net.lab0.skyscrapers.server.dto.TurnTypeDTO.Companion.win(
        win = net.lab0.skyscrapers.server.dto.WinTurnDTO(
          1,
          net.lab0.skyscrapers.server.dto.PositionDTO(2, 3),
          net.lab0.skyscrapers.server.dto.PositionDTO(4, 5),
        )
      ).toTurnType()
    ).isEqualTo(
      net.lab0.skyscrapers.engine.api.TurnType.MoveTurn.WinTurn(
        1,
        Position(2, 3),
        Position(4, 5),
      )
    )
  }

  // TODO: test only 1 type of turn possible in a turn type DTO.
  //  Needs to be done via deserialization of json as no constructor allows that.
}