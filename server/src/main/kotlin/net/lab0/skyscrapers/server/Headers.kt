package net.lab0.skyscrapers.server

import org.http4k.lens.Header

val Header.AUTHORIZATION
  get() = Header.map(
    Authorization::Bearer,
    Authorization.Bearer::getHeaderValue
  ).required("Authorization")

sealed class Authorization {
  data class Basic(val user: String, val password: String)
  data class Bearer(val token: String) {
    fun getHeaderValue() = "Bearer: $token"
  }
}
