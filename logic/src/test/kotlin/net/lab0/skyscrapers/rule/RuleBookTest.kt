package net.lab0.skyscrapers.rule

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RuleBookTest {
  @Test
  fun `placement rules has size 2`() {
    val ruleBook = RuleBook()
    assertThat(ruleBook.placementRules).hasSize(2)
  }
}