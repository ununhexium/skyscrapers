package net.lab0.skyscrapers.client.clikt

import org.junit.jupiter.api.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import java.nio.file.Path
import java.nio.file.Paths


class B(val a: Path)

class KoinFailTest : KoinTest {

  @Test
  fun works() {
    startKoin {
      modules(
        module {
          single(named("a")) { Paths.get("") }
          single { B(get(named("a"))) }
        }
      )
      checkModules()
    }

    stopKoin()
  }

  @Test
  fun fails() {

    startKoin {
      modules(
        module {
          single(QUALIFIER) { Paths.get("") }
          single { B(get(QUALIFIER)) }
        }
      )
      checkModules()
    }

    stopKoin()
  }

  val QUALIFIER = named("x")
}
