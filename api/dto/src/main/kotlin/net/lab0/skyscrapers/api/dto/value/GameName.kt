package net.lab0.skyscrapers.api.dto.value

import net.lab0.skyscrapers.api.structure.Valued

@JvmInline
value class GameName(override val value:String): Valued<String> {
  override fun toString() = value

  companion object {
    val EMPTY = GameName("")
  }
}
