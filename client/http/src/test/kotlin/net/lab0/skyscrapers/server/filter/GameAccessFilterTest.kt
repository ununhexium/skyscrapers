package net.lab0.skyscrapers.server.filter

import io.mockk.mockk
import net.lab0.skyscrapers.server.Service
import org.junit.jupiter.api.Test

internal class GameAccessFilterTest {
  @Test
  fun `let the user access the game api when it's authorized`() {
    val service = mockk<Service>()
    val filter = GameAccessFilter(service)

    // TODO: add test
  }
}