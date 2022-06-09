package net.lab0.skyscrapers.api.structure

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.Test

internal class PositionTest {
  @Test
  fun `get surrounding positions`() {
    Position(0, 0)
      .getSurroundingPositions() shouldContainExactlyInAnyOrder listOf(
      Position(1, 0),
      Position(-1, 0),
      Position(0, 1),
      Position(0, -1),

      Position(1, 1),
      Position(-1, -1),
      Position(-1, 1),
      Position(1, -1),
    )
  }
}
