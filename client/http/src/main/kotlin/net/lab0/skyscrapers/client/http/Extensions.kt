package net.lab0.skyscrapers.client.http

import net.lab0.skyscrapers.engine.Valued
import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Uri
import org.http4k.core.extend
import org.http4k.format.KotlinxSerialization.auto

operator fun Uri.div(s: String): Uri =
  this.extend(Uri.of(s))

operator fun Uri.div(s: Valued<String>): Uri =
  this.extend(Uri.of(s.value))

operator fun String.div(s: String): String =
  if (this.endsWith('/')) this + s else "$this/$s"

operator fun String.div(s: Valued<String>): String =
  if (this.endsWith('/')) this + s.value else "$this/${s.value}"

inline fun <reified T : Any> Response.extract(): T =
  Body.auto<T>().toLens().extract(this)

typealias Errors = List<String>
