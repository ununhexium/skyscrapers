package dependencies

object TestDependencies {
  val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"

  val junit5Api = "org.junit.jupiter:junit-jupiter-api:${Versions.jupiter}"
  val junit5Params = "org.junit.jupiter:junit-jupiter-params:${Versions.jupiter}"
  val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.jupiter}"

  val mockK = "io.mockk:mockk:${Versions.mockk}"
}
