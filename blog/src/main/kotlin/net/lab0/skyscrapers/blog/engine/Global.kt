package net.lab0.skyscrapers.blog.page

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("M/d/yy K:mm a")

fun asInstant(s:String) =
  Instant.from(LocalDate.parse(s, formatter).atStartOfDay(ZoneOffset.UTC))
