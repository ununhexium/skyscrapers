package net.lab0.skyscrapers.blog.page

import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.ul
import net.lab0.skyscrapers.blog.engine.KotlinxHtmlRawBody
import net.lab0.skyscrapers.blog.engine.PageImpl
import net.lab0.skyscrapers.blog.engine.Publication
import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset

object Introduction : Publication {
  override val id = "5/3/22 10:51 PM"
  override val after = Instant.MIN!!
  override val lastUpdate = LocalDateTime.of(2022, Month.MAY, 3, 20, 0)
    .toInstant(ZoneOffset.UTC)

  override val page = object : PageImpl() {

    override val title = "Introduction"

    override val body = KotlinxHtmlRawBody {
      div {
        h1 {
          +"The Skyscrapers project"
        }

        p {
          +"""
          The objective is to implement a small game where the goal is to build
           and reach the highest position before your opponents.
           
           The challenge will be to use Kotlin only for all the parts of 
           the project. That is:
        """.trimIndent()
        }

        ul {
          li { +"The game engine" }
          li { +"The game's web UI" }
          li { +"The game's CLI" }
          li { +"The game's blog (this)" }
          li { +"Favour Kotlin libraries when they exist" }
        }

        p {
          +"""
          Coming from backend development, the objective will be for me to 
          get better at writing web UIs.
        """.trimIndent()
        }
      }
    }
  }
}
