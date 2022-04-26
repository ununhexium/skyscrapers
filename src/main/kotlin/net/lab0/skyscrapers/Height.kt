package net.lab0.skyscrapers

@JvmInline
value class Height(val value: Int) {
  operator fun inc() =
    Height(value + 1)

  operator fun plus(amount:Int) =
    Height(value+1)
}
