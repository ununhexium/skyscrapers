package net.lab0.skyscrapers.api.dto

import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.api.structure.Position
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class TurnTypeDTOTest {
  @Test
  fun `can deserialize a give up turn`() {
    Assertions.assertThat(
      TurnTypeDTO.giveUp(giveUp = GiveUpTurnDTO(116))
        .toTurnType()
    ).isEqualTo(
      TurnType.GiveUpTurn(116)
    )
  }

  @Test
  fun `can deserialize a place turn`() {
    Assertions.assertThat(
      TurnTypeDTO.place(
        place = PlaceTurnDTO(
          1,
          PositionDTO(2, 3)
        )
      )
        .toTurnType()
    ).isEqualTo(
      TurnType.PlacementTurn(1, Position(2, 3))
    )
  }

  @Test
  fun `can deserialize a build turn`() {
    Assertions.assertThat(
      TurnTypeDTO.build(
        build = BuildTurnDTO(
          1,
          PositionDTO(2, 3),
          PositionDTO(4, 5),
          PositionDTO(6, 7),
        )
      ).toTurnType()
    ).isEqualTo(
      TurnType.MoveTurn.BuildTurn(
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
      TurnTypeDTO.seal(
        seal = SealTurnDTO(
          1,
          PositionDTO(2, 3),
          PositionDTO(4, 5),
          PositionDTO(6, 7),
        )
      ).toTurnType()
    ).isEqualTo(
      TurnType.MoveTurn.SealTurn(
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
      TurnTypeDTO.win(
        win = WinTurnDTO(
          1,
          PositionDTO(2, 3),
          PositionDTO(4, 5),
        )
      ).toTurnType()
    ).isEqualTo(
      TurnType.MoveTurn.WinTurn(
        1,
        Position(2, 3),
        Position(4, 5),
      )
    )
  }

  // TODO: test only 1 type of turn possible in a turn type DTO.
  //  Needs to be done via deserialization of json as no constructor allows that.
}