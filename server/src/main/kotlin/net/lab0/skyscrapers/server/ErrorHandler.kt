package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.api.http4k.AUTH_MISSING_MESSAGE
import net.lab0.skyscrapers.api.http4k.InvalidAuthentication
import org.http4k.filter.ServerFilters
import org.http4k.lens.LensFailure

val errorHandler = ServerFilters.CatchAll { ex ->
  when (ex) {
    is Error -> throw ex // non-recoverable
    is LensFailure -> if (ex.failures.firstOrNull()?.meta?.description == AUTH_MISSING_MESSAGE) {
      unauthorized(
        "Can't access this game: give a Authorization: Bearer ... " +
            "header that you got when connecting to access the game."
      )
    } else {
      internalServerError(ex.message)
    }
    is InvalidAuthentication -> unauthorized(ex.message)
    else -> internalServerError(ex.message)
  }
}
