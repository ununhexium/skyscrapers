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
class BuildFromValueProvider(
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
  ) =
    serverAccessManager.buildFromCompletion().map {
      CompletionProposal(it.toString(COMA))
    }
}
