package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand


fun CliktCommand.parse(vararg s: String) {
  this.parse(s.toList())
}
