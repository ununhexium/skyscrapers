package net.lab0.skyscrapers.api.http4k

import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.http4k.Authorization.Bearer
import net.lab0.skyscrapers.api.http4k.Authorization.Companion.BEARER_PREFIX
import org.http4k.core.HttpMessage
import org.http4k.lens.BiDiLens
import org.http4k.lens.Header


const val AUTH_MISSING_MESSAGE = "Authorization header is missing."

val Header.AUTHORIZATION: BiDiLens<HttpMessage, Bearer>
  get() {
    return Header.map(
      {
        try {
          Bearer(AccessToken(it.substring(BEARER_PREFIX.length)))
        } catch (e: Exception) {
          throw InvalidAuthentication(it)
        }
      },
      Bearer::getHeaderValue
    ).required("Authorization", AUTH_MISSING_MESSAGE)
  }

sealed class Authorization {
  companion object {
    const val BEARER_PREFIX = "Bearer: "
  }

  data class Bearer(val token: AccessToken) {
    fun getHeaderValue() = "${BEARER_PREFIX}${token.value}"
  }
}
