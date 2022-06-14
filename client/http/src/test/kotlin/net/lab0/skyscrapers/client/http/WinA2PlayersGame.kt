package net.lab0.skyscrapers.client.http

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Phase
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.FakeServerTest
import org.junit.jupiter.api.Test

class WinA2PlayersGame : FakeServerTest {

  @Test
  fun `can win a 2 players game`() {

    fakeServer { handler ->
      val client = SkyscraperClientImpl(handler)

      // create game
      val foo = GameName("foo")
      client.create(foo)

      // connect as player 0
      val player0 = client.join(foo).shouldBeRight()

      // connect as player 1
      val player1 = client.join(foo).shouldBeRight()

      println(player0)
      println(player1)

      //turn0: GameResponse =
      client.place(foo, player0.token, Position(1, 1))
      //turn1: GameResponse =
      client.place(foo, player1.token, Position(3, 1))
      //turn2: GameResponse =
      client.place(foo, player0.token, Position(3, 3))
      //turn3: GameResponse =
      client.place(foo, player1.token, Position(1, 3))

      // build round trip 1
      //turn4: GameResponse =
      client.build(foo, player0.token, Position(1, 1), Position(2, 1), Position(1, 1))
      //turn5: GameResponse =
      client.build(foo, player1.token, Position(3, 1), Position(3, 2), Position(3, 1))
      //turn6: GameResponse =
      client.build(foo, player0.token, Position(2, 1), Position(1, 1), Position(2, 1))
      //turn7: GameResponse =
      client.build(foo, player1.token, Position(3, 2), Position(3, 1), Position(3, 2))

      // build round trip 2
      //turn8: GameResponse =
      client.build(foo, player0.token, Position(1, 1), Position(2, 1), Position(1, 1))
      //turn9: GameResponse =
      client.build(foo, player1.token, Position(3, 1), Position(3, 2), Position(3, 1))
      //turn10: GameResponse =
      client.build(foo, player0.token, Position(2, 1), Position(1, 1), Position(2, 1))
      //turn11: GameResponse =
      client.build(foo, player1.token, Position(3, 2), Position(3, 1), Position(3, 2))

      // build round trip 3
      //turn12: GameResponse =
      client.build(foo, player0.token, Position(1, 1), Position(2, 1), Position(1, 1))
      //turn13: GameResponse =
      client.build(foo, player1.token, Position(3, 1), Position(3, 2), Position(3, 1))
      //turn14: GameResponse =
      client.win(foo, player0.token, Position(2, 1), Position(1, 1))

      // player0 wins
      val finalResponse = client.state(foo).shouldBeRight()
      finalResponse.phase shouldBe Phase.FINISHED
    }
  }
}