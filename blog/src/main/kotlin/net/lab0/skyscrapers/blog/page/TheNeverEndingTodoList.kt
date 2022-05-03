package net.lab0.skyscrapers.blog.page

import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
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


object TheNeverEndingTodoList : Publication {
  override val id = "5/3/22 10:53 PM"
  override val after = Instant.MIN!!

  override val lastUpdate = LocalDateTime
    .of(2022, Month.MAY, 3, 20, 40)
    .toInstant(ZoneOffset.UTC)

  override val page = object : PageImpl() {
    override val title = "The forever ongoing ToDo list of this project"
    override val body = KotlinxHtmlRawBody {
      h1 { +"ToDo" }

      ul {
        li { +"Stupid 'AI'" }
        li { +"CLI client" }
        li { +"Web client" }
      }

      h2 { +"Challenge" }
      h3 { +"One blog article per week" }
      p { +"Write one article per week (minimum) to explain the development process of the game." }
    }
  }
}
