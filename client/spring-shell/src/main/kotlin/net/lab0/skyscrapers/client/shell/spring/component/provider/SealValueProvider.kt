package net.lab0.skyscrapers.client.shell.spring.component.provider

import net.lab0.skyscrapers.api.structure.Position.Style.COMA
import net.lab0.skyscrapers.client.shell.spring.Key
import net.lab0.skyscrapers.client.shell.spring.component.ServerAccessManager
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProvider
import org.springframework.stereotype.Component

@Component
class SealValueProvider(
  val serverAccessManager: ServerAccessManager
) : ValueProvider {

  override fun supports(
    parameter: MethodParameter,
    completionContext: CompletionContext?
  ) = parameter.executable.name == Key.Command.seal

  override fun complete(
    parameter: MethodParameter,
    completionContext: CompletionContext,
    hints: Array<out String>?
  ): List<CompletionProposal> {

    val from = valueOfParam(completionContext, "--from")
    val to = valueOfParam(completionContext, "--to")
    val seal = valueOfParam(completionContext, "--seal")

    val turns = serverAccessManager.sealCompletion(
      from = from,
      to = to,
      seal = seal,
    )

    return when (parameter.parameterName) {
      "from" -> turns.map {
        CompletionProposal(it.start.toString(COMA))
      }
      "to" -> turns.map {
        CompletionProposal(it.target.toString(COMA))
      }
      "seal" -> turns.map {
        CompletionProposal(it.seal.toString(COMA))
      }
      else -> listOf()
    }
  }
}
