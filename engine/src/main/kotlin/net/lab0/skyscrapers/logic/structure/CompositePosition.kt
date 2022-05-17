package net.lab0.skyscrapers.logic.structure

data class CompositePosition(
  val building: Int,
  val seal: Boolean,
  val builder: Int?
) {

  companion object {

    operator fun Regex.contains(s: String) = s.matches(this)

    private val SEALED = Regex("\\((?<HEIGHT>\\d+)\\)")
    private val BUILDER = Regex("(?<PLAYER>[A-Z])(?<HEIGHT>\\d+)")
    private val BUILDING = Regex("(?<HEIGHT>\\d+)")

    fun from(input: String): CompositePosition {

      SEALED.matchEntire(input)?.let {
        return CompositePosition(
          it.groups["HEIGHT"]!!.value.toInt(),
          true,
          null
        )
      }

      BUILDER.matchEntire(input)?.let {
        return CompositePosition(
          it.groups["HEIGHT"]!!.value.toInt(),
          false,
          it.groups["PLAYER"]!!.value.first().code - 'A'.code
        )
      }

      BUILDING.matchEntire(input)?.let {
        return CompositePosition(
          it.groups["HEIGHT"]!!.value.toInt(),
          false,
          null
        )
      }?: throw IllegalArgumentException(
        "Can't parse $input as a composite position"
      )
    }
  }
}
