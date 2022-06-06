package net.lab0.skyscrapers.client.clikt

import org.junit.jupiter.api.Test

internal class ApplicationKtTest {
  @Test
  fun `check that the cli can run`() {
    main(arrayOf("version"))
  }
}
