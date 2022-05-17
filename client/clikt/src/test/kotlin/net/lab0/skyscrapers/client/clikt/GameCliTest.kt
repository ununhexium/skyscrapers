package net.lab0.skyscrapers.client.clikt

import net.lab0.skyscrapers.logic.api.Game
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.atomic.AtomicReference

internal class GameCliTest {
  lateinit var ref: AtomicReference<Game?>

  @BeforeEach
  fun beforeEach() {
    ref = AtomicReference(null)
  }

  @Test
  fun `can start a new game with params`() {
    GameCli.new(ref, PrintWriter(StringWriter())).main(
      listOf(
        "new", "--width=9", "--height=8", "--players=7", "--builders=6"
      )
    )

    val game = ref.get()!!
    Assertions.assertThat(game.state.maxBuildersPerPlayer).isEqualTo(6)
    Assertions.assertThat(game.state.players).hasSize(7)
    Assertions.assertThat(game.state.bounds.height).isEqualTo(8)
    Assertions.assertThat(game.state.bounds.width).isEqualTo(9)
  }
}
