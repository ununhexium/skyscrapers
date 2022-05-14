package dependencies

object TestDependencies {
  val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"

  val junit5_api = "org.junit.jupiter:junit-jupiter-api:${Versions.jupiter}"
  val junit5_params = "org.junit.jupiter:junit-jupiter-params:${Versions.jupiter}"
  val junit5_engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.jupiter}"

  val mockk = "io.mockk:mockk:${Versions.mockk}"
}
