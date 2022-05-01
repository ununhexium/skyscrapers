import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Fail {
  @Test
  fun `fail`() {
    assertThat(true).isFalse()
  }
}