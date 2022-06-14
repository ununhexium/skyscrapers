package net.lab0.skyscrapers.client.shell.spring.component

import arrow.core.Either.Right
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.client.shell.spring.BaseUrl
import net.lab0.skyscrapers.client.shell.spring.SkyscraperClientFactoryComponent
import org.junit.jupiter.api.Test

internal class GameAccessManagerTest {
  private val baseUrl = BaseUrl("http://example.com:116/")
  private val baseUrl2 = BaseUrl("http://example2.com:117/")

  @Test
  fun `start unconnected and outside of any game`() {
    val factory = mockk<SkyscraperClientFactoryComponent>()
    val subject = GameAccessManager(factory)

    subject.inGame shouldBe false
    subject.currentGame shouldBe null
  }

  @Test
  fun `can (re)connect`() {
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk()
      every { newClient(baseUrl2) } returns mockk()
    }

    val subject = GameAccessManager(factory)
    subject.reconnect(baseUrl)

    verify { factory.newClient(baseUrl) }
    subject.isConnected() shouldBe true
    subject.inGame shouldBe false

    subject.reconnect(baseUrl2)

    verify { factory.newClient(baseUrl2) }
    subject.isConnected() shouldBe true
    subject.inGame shouldBe false
  }

  @Test
  fun `failsafe status when not connected`() {
    val factory = mockk<SkyscraperClientFactoryComponent>()

    val subject = GameAccessManager(factory)

    subject.status() shouldBe "Not connected."
  }

  @Test
  fun `status when connected and no game is available`() {
    // given
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk() {
        every { status() } returns Right(StatusResponse("up", emptySet()))
      }
    }
    val subject = GameAccessManager(factory)
    subject.reconnect(baseUrl)

    // then
    subject.status() shouldBe "No game available."
  }

  @Test
  fun `status when connected with available games`() {
    // given
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk() {
        every { status() } returns Right(StatusResponse("up", setOf("a", "z", "b", "d")))
      }
    }
    val subject = GameAccessManager(factory)
    subject.reconnect(baseUrl)

    // then
    subject.status() shouldBe """
      |Available games:
      |a
      |b
      |d
      |z
    """.trimMargin()
  }

  // TODO continue
}