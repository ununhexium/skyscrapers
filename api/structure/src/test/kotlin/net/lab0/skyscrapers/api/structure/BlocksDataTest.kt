package net.lab0.skyscrapers.api.structure

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class BlocksDataTest {
  @Test
  fun `show the blocks`() {
    val data = BlocksData(
      Height(0) to 15,
      Height(1) to 10,
      Height(2) to 5,
    )

    data.toShortString() shouldBe "0:15, 1:10, 2:5"
  }
}