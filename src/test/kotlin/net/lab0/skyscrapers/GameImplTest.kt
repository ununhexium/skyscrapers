package net.lab0.skyscrapers

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class GameImplTest {
  @TestFactory
  fun `the initial height of the buildings is 0`(): Iterable<DynamicTest> {
    val g = GameImpl(3, 4, 2)

    return (0..2).flatMap{x ->
      (0..3).map{y ->
        DynamicTest.dynamicTest("g[$x,$y] == 0") {
          Assertions.assertThat(g[x, y]).isEqualTo(0)
        }
      }
    }
  }


}