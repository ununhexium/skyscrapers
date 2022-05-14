package dependencies

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Dependencies {
//  val material = "com.google.android.material:material:${Versions.material}"
    val clikt = "com.github.ajalt.clikt:clikt:${Versions.clikt}"

    val jline = "org.jline:jline:${Versions.jline}"
    val jlineBuiltins = "org.jline:jline-builtins:${Versions.jline}"
}

fun DependencyHandler.clikt() {
    add("implementation", "com.github.ajalt.clikt:clikt:${Versions.clikt}")
}
