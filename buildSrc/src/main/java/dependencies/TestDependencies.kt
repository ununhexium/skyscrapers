package dependencies

// @formatter:off
object TestDependencies {
  val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"

  object http4k {
    val kotest = Dependencies.http4k("testing-kotest")
  }

  val junit5Api = "org.junit.jupiter:junit-jupiter-api:${Versions.jupiter}"
  val junit5Params = "org.junit.jupiter:junit-jupiter-params:${Versions.jupiter}"
  val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.jupiter}"

  object kotest {
    val core = "io.kotest:kotest-assertions-core:${Versions.kotest}"
    val arrowJvm = "io.kotest.extensions:kotest-assertions-arrow-jvm:${Versions.kotestArrowJvm}"
  }

  val mockK = "io.mockk:mockk:${Versions.mockk}"
}
