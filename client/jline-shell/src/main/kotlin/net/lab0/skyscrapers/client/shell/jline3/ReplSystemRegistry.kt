package net.lab0.skyscrapers.client.shell.jline3

import org.jline.builtins.ConfigurationPath
import org.jline.console.impl.SystemRegistryImpl
import org.jline.reader.Parser
import org.jline.terminal.Terminal
import java.nio.file.Path
import java.util.function.Supplier

class ReplSystemRegistry(
  parser: Parser?,
  terminal: Terminal?,
  workDir: Supplier<Path>?,
  configPath: ConfigurationPath?
) : SystemRegistryImpl(parser, terminal, workDir, configPath) {
  override fun isCommandOrScript(command: String): Boolean {
    return command.startsWith("!") || super.isCommandOrScript(command)
  }
}
