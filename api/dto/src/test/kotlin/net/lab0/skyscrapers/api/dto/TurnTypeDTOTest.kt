package net.lab0.skyscrapers.api.dto

import net.lab0.skyscrapers.api.structure.TurnType
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

  // TODO: test only 1 type of turn possible in a turn type DTO.
  //  Needs to be done via deserialization of json as no constructor allows that.
}