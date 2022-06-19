package net.lab0.skyscrapers.api.dto

import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.api.structure.BlocksData
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.api.structure.Player
import org.junit.jupiter.api.Test

internal class BlocksDataDTOTest {
  @Test
  fun `map to DTO`() {
    // given
    val state = GameState.from(
      listOf(Player(0, true), Player(1, false)),
      2,
      BlocksData(Height(0) to 5, Height(1) to 10, Height(2) to 5),
      """
        |0 0
        |0 0
      """.trimMargin(),
      """
        |false true
        |false true
      """.trimMargin(),
      """
        |1 .
        |. 0
      """.trimMargin()
    )

    // when
    val serialized = GameStateDTO(state)
    val deserialized = serialized.toModel()

    // then
    deserialized shouldBe state
  }
}
