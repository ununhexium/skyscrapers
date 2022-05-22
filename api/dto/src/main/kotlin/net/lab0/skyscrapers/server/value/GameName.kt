package net.lab0.skyscrapers.server.value

import net.lab0.skyscrapers.engine.Valued

@JvmInline
value class GameName(override val value:String): Valued<String> {
  override fun toString() = value
}
