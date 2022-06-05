package net.lab0.skyscrapers.client.clikt

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

open class KoinBase : KoinTest {

  @BeforeEach
  fun beforeEach() {
    startKoin {
      modules(cliModule)
    }
  }

  @AfterEach
  fun afterEach() {
    stopKoin()
  }
}