package net.lab0.skyscrapers.client.clikt

import net.lab0.skyscrapers.engine.api.Series
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import java.io.StringWriter

internal class GameCliTest {
  lateinit var series: Series

  @BeforeEach
  fun beforeEach() {
    series = Series.newBestOf1()
  }

  @Test
  fun `can start a new game with params`() {
    GameCli.new(series, PrintWriter(StringWriter())).main(
      listOf(
        "next"
      )
    )

    assertThat(series.getCurrentRound()).isNotNull
  }
}
