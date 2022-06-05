package net.lab0.skyscrapers.client.clikt

import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.di.CONFIG_PATH
import net.lab0.skyscrapers.client.clikt.di.cliModule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

open class KoinBase(val addDefaultConfigurer: Boolean = false) : KoinTest {

  @BeforeEach
  fun beforeEach() {
    startKoin {
      modules(
        cliModule,
        module {
          if (addDefaultConfigurer) {
            single { Configurer(get(CONFIG_PATH)) }
          }
        }
      )
    }
  }

  @AfterEach
  fun afterEach() {
    stopKoin()
  }
}
