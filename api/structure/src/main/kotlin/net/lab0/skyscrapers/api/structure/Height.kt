package net.lab0.skyscrapers.api.structure

@JvmInline
value class Height(val value: Int): Comparable<Height> {
  companion object {
    val SEAL = Height(0)
  }

  operator fun inc() =
    Height(value + 1)

  operator fun plus(amount:Int) =
    Height(value + amount)

  operator fun minus(other: Height) =
    Height(this.value - other.value)

  operator fun compareTo(height: Int) =
    value.compareTo(height)

  override operator fun compareTo(other: Height) =
    value.compareTo(other.value)
}
