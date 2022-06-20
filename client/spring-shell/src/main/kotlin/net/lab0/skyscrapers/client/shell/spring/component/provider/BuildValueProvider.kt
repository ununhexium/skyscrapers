package net.lab0.skyscrapers.client.shell.spring.component.provider

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.Position.Style.COMA
import net.lab0.skyscrapers.client.shell.spring.Key
import net.lab0.skyscrapers.client.shell.spring.component.ServerAccessManager
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProvider
import org.springframework.stereotype.Component

@Component
class BuildValueProvider(
  val serverAccessManager: ServerAccessManager
) : ValueProvider {

  override fun supports(
    parameter: MethodParameter,
    completionContext: CompletionContext?
  ) = parameter.executable.name == Key.Command.build

  override fun complete(
    parameter: MethodParameter,
    completionContext: CompletionContext,
    hints: Array<out String>?
  ): List<CompletionProposal> {
    val words = completionContext.words

    fun valueOfParam(paramName: String) =
      words.indexOf(paramName).let {
        if (it < 0) null else words.getOrNull(it + 1)
      }?.let {
        if (it.contains(",")) Position.fromComaStyle(it)
        else null
      }

    val from = valueOfParam("--from")
    val to = valueOfParam("--to")
    val build = valueOfParam("--build")

    val turns = serverAccessManager.buildCompletion(
      from = from,
      to = to,
      build = build,
    )

    return when (parameter.parameterName) {
      "from" -> turns.map {
        CompletionProposal(it.start.toString(COMA))
      }
      "to" -> turns.map {
        CompletionProposal(it.target.toString(COMA))
      }
      "build" -> turns.map {
        CompletionProposal(it.build.toString(COMA))
      }
      else -> listOf()
    }
  }
}
