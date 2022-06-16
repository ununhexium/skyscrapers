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
class PlaceAtValueProvider(val serverAccessManager: ServerAccessManager) :
  ValueProvider {
  override fun supports(
    parameter: MethodParameter,
    completionContext: CompletionContext
  ) = parameter.executable.name == Key.Command.place

  override fun complete(
    parameter: MethodParameter,
    completionContext: CompletionContext,
    hints: Array<out String>?
  ) = serverAccessManager
    .placeAtCompletion()
    .map { CompletionProposal(it.toString(COMA)) }
}
