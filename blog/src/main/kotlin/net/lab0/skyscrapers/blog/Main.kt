package net.lab0.skyscrapers.blog

import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.li
import kotlinx.html.stream.appendHTML
import kotlinx.html.ul
import net.lab0.skyscrapers.blog.page.Foundation
import net.lab0.skyscrapers.blog.page.GameDesign
import net.lab0.skyscrapers.blog.page.Introduction
import net.lab0.skyscrapers.blog.page.TheNeverEndingTodoList
import spark.kotlin.ignite
import java.security.MessageDigest

val ARTICLES_PATH_SEGMENT = "/blog/article"
val RESOURCES_PATH_SEGMENT = "/resources"
val IMAGES_PATH_SEGMENT = "$RESOURCES_PATH_SEGMENT/images"

fun main(args: Array<String>) {
  val http = ignite()

  val articles = listOf(
    Introduction,
    TheNeverEndingTodoList,
    GameDesign,
    Foundation,
  )

  val digest = MessageDigest.getInstance("SHA256")

  val references = articles.associateBy { pub ->
    digest
      .digest(pub.id.toByteArray())
      .joinToString(separator = "") { it.toUByte().toString(16) }
  }

  http.get("/index.html") {
    StringBuffer().appendHTML(true).html {
      body {
        ul {
          references.forEach { (id, pub) ->
            li {
              a(href = "$ARTICLES_PATH_SEGMENT/$id") { +pub.page.title }
            }
          }
        }
      }
    }
  }

  http.get("$ARTICLES_PATH_SEGMENT/:id") {
    request.params("id")?.let { id ->
      references[id]?.page?.build()
        ?: "Didn't find the page with id $id"
    } ?: "id is missing"
  }

  http.get("$IMAGES_PATH_SEGMENT/:article/:name") {
    val article = request.params("article")
    val name = request.params("name")

    val binary =
      Root::class.java.getResourceAsStream("/img/$article/$name")?.readAllBytes()
        ?: ByteArray(0)

    response.header("Content-Disposition", """attachment; filename="$name"""")
    response.type("application/octet-stream")
    response.raw().setContentLength(binary.size)
    response.status(200)

    response.raw().outputStream.use{
      it.write(binary)
    }
  }

  println("Started on port ${http.port()}")
}
