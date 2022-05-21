package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import java.io.Writer

abstract class MyCliktCommand(
  val writer: Writer?,
  help: String = "",
  epilog: String = "",
  name: String? = null,
  invokeWithoutSubcommand: Boolean = false,
  printHelpOnEmptyArgs: Boolean = false,
  helpTags: Map<String, String> = emptyMap(),
  autoCompleteEnvvar: String? = "",
  allowMultipleSubcommands: Boolean = false,
  treatUnknownOptionsAsArgs: Boolean = false,
) : CliktCommand(
  help = help,
  epilog = epilog,
  name = name,
  invokeWithoutSubcommand = invokeWithoutSubcommand,
  printHelpOnEmptyArgs = printHelpOnEmptyArgs,
  helpTags = helpTags,
  autoCompleteEnvvar = autoCompleteEnvvar,
  allowMultipleSubcommands = allowMultipleSubcommands,
  treatUnknownOptionsAsArgs = treatUnknownOptionsAsArgs,
) {
  /**
   * testable echo
   */
  fun myEcho(string: String) {
    if (writer == null) {
      echo(string)
    } else {
      writer.write(string)
    }
  }
}
