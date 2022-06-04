package net.lab0.skyscrapers.api.dto.value

@JvmInline
value class GameName(val value:String) {
  override fun toString() = value

  companion object {
    val EMPTY = GameName("")
  }
}
