package net.lab0.skyscrapers.structure

@JvmInline
value class Height(val value: Int) {
  companion object {
    val SEAL = Height(0)
  }

  operator fun inc() =
    Height(value + 1)

  operator fun plus(amount:Int) =
    Height(value+1)

  operator fun minus(other: Height) =
    Height(this.value - other.value)

  operator fun compareTo(height: Int) =
    value.compareTo(height)

  operator fun compareTo(height: Height) =
    value.compareTo(height.value)
}
