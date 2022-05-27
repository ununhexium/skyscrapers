package net.lab0.skyscrapers.client.clikt.command

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.client.FakeServerTest
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import net.lab0.skyscrapers.api.dto.value.GameName
import org.junit.jupiter.api.Test

internal class NewGameTest : FakeServerTest {
  @Test
  fun `can create a new game`() {
    fakeServer { handler ->
      val client = SkyscraperClientImpl(handler)
      val out = client.create(GameName("Springfield")).shouldBeRight()
      out.name shouldBe "Springfield"
    }
  }
}
