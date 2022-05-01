package net.lab0.skyscrapers.structure

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CompositePositionTest {
  @Test
  fun `can parse a building position`() {
    assertThat(CompositePosition.from("116")).isEqualTo(
      CompositePosition(116, false, null)
    )
  }

  @Test
  fun `can parse a sealed position`() {
    assertThat(CompositePosition.from("(5)")).isEqualTo(
      CompositePosition(5, true, null)
    )
  }

  @Test
  fun `can parse a builder's position`() {
    assertThat(CompositePosition.from("Z1")).isEqualTo(
      CompositePosition(1, false, 25)
    )
  }
}