package net.lab0.skyscrapers.client.shell.spring.component

import arrow.core.Either.Left
import arrow.core.Either.Right
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.ConnectionResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.client.shell.spring.BaseUrl
import net.lab0.skyscrapers.client.shell.spring.SkyscraperClientFactoryComponent
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult.Ok
import net.lab0.skyscrapers.client.shell.spring.data.ShellResult.Problem
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

    subject.status() shouldBe Problem.Text("Not connected.")
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
    subject.status() shouldBe Ok.Text("No game available.")
  }

  @Test
  fun `status when connected with available games`() {
    // given
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk() {
        every { status() } returns Right(
          StatusResponse(
            "up",
            setOf("a", "z", "b", "d")
          )
        )
      }
    }
    val subject = GameAccessManager(factory)
    subject.reconnect(baseUrl)

    // then
    subject.status() shouldBe Ok.Text(
      """
      |Available games:
      |a
      |b
      |d
      |z
    """.trimMargin()
    )
  }

  @Test
  fun `failsafe game creation when not connected`() {
    // given
    val foo = GameName("foo")
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk()
    }
    val subject = GameAccessManager(factory)

    // then
    subject.create(foo) shouldBe Problem.Text("Connect to a server before creating a game.")
  }

  @Test
  fun `game creation when no game with that name exists`() {
    // given
    val foo = GameName("foo")
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk {
        every { create(foo) } returns Right(GameResponse(foo, GameState.DUMMY))
      }
    }
    val subject = GameAccessManager(factory)
    subject.reconnect(baseUrl)

    // then
    subject.create(foo) shouldBe Ok.Text("Created the game foo.")
  }

  @Test
  fun `game creation when a game with the same name already exists`() {
    // given
    val foo = GameName("foo")
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk {
        every { create(foo) } returns Left(
          listOf(
            "ERROR MESSAGE 1",
            "ERROR MESSAGE 2"
          )
        )
      }
    }
    val subject = GameAccessManager(factory)
    subject.reconnect(baseUrl)

    // then
    subject.create(foo) shouldBe Problem.Text(
      """
      |Error when creating the game:
      |ERROR MESSAGE 1
      |ERROR MESSAGE 2
    """.trimMargin()
    )
  }

  @Test
  fun `failsafe join when not connected`() {
    // given
    val foo = GameName("foo")
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk()
    }
    val subject = GameAccessManager(factory)

    // then
    subject.join(foo) shouldBe Problem.Text("Connect to a server before joining a game.")
  }

  @Test
  fun `join a game that is not full`() {
    // given
    val foo = GameName("foo")
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk {
        every { join(foo) } returns
            Right(ConnectionResponse(0, AccessToken("TOKEN")))
      }
    }
    val subject = GameAccessManager(factory)
    subject.reconnect(baseUrl)

    // then
    subject.join(foo) shouldBe Ok.Text("Joined the game foo as player 0 with access token TOKEN.")
  }

  @Test
  fun `join a game that returns an erro`() {
    // given
    val foo = GameName("foo")
    val factory = mockk<SkyscraperClientFactoryComponent>() {
      every { newClient(baseUrl) } returns mockk {
        every { join(foo) } returns
            Left(listOf("ERROR MESSAGE 1", "ERROR MESSAGE 2"))
      }
    }
    val subject = GameAccessManager(factory)
    subject.reconnect(baseUrl)

    // then
    subject.join(foo) shouldBe Problem.Text("""
      |Error when joining the game foo:
      |ERROR MESSAGE 1
      |ERROR MESSAGE 2
    """.trimMargin())
  }

}
