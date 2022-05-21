package net.lab0.skyscrapers.server

import io.kotest.matchers.Matcher
import io.kotest.matchers.be
import org.http4k.core.Body
import org.http4k.core.HttpMessage
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.kotest.LensMatcher
import org.http4k.kotest.httpMessageHas

inline fun <T : HttpMessage, reified B : Any> haveResponseBe(responseBody: B): Matcher<T> =
  LensMatcher(
    httpMessageHas(
      "Body",
      { m: T -> Body.auto<B>().toLens().extract(m) },
      be(responseBody)
    )
  )
