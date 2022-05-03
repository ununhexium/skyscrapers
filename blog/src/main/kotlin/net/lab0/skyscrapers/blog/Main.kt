package net.lab0.skyscrapers.blog

import net.lab0.skyscrapers.blog.page.Introduction
import spark.kotlin.ignite

fun main(args: Array<String>) {
  val http = ignite()

  http.get("/index.html") {
    Introduction.page.build()
  }

  println("Started on port ${http.port()}")
}
