package net.lab0.skyscrapers.client.http

import net.lab0.skyscrapers.api.structure.Valued
import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Uri
import org.http4k.core.extend
import org.http4k.core.toPathSegmentEncoded
import org.http4k.format.KotlinxSerialization.auto

operator fun Uri.div(s: String): Uri =
  this.extend(Uri.of(s))

operator fun Uri.div(s: Valued<String>): Uri =
  this.extend(Uri.of(s.value))

operator fun String.div(s: String): String =
  if (this.endsWith('/'))
    this + s.toPathSegmentEncoded()
  else
    "$this/${s.toPathSegmentEncoded()}"

operator fun String.div(s: Valued<String>): String =
  this.div(s.value)

inline fun <reified T : Any> Response.extract(): T =
  Body.auto<T>().toLens().extract(this)

typealias Errors = List<String>
