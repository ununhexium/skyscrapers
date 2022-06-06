package net.lab0.skyscrapers.api.structure

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class BoundsTest {

  @Test
  fun `can get a positions sequence`() {
    val positions = Bounds(0..1, 0..2).positionsSequence.toList()
    positions shouldContainExactlyInAnyOrder listOf(
      Position(0, 0),
      Position(0, 1),
      Position(0, 2),
      Position(1, 0),
      Position(1, 1),
      Position(1, 2),
    )
  }

  @Test
  fun `contain a point`() {
    Bounds(0..1, 0..2).contains(Position(1, 1)) shouldBe true
  }

  @Test
  fun `doesnt contain a point`() {
    // too low
    val bounds = Bounds(0..1, 0..2)

    bounds.contains(Position(1, -1)) shouldBe false
    // too height
    bounds.contains(Position(1, 99)) shouldBe false

    // too left
    bounds.contains(Position(-1, 1)) shouldBe false
    // too right
    bounds.contains(Position(99, 1)) shouldBe false
  }
}
