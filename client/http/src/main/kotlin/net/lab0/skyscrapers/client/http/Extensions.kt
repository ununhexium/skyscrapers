package net.lab0.skyscrapers.client.http

import org.http4k.core.Uri
import org.http4k.core.extend

operator fun Uri.div(s: String): Uri =
  this.extend(Uri.of(s))

