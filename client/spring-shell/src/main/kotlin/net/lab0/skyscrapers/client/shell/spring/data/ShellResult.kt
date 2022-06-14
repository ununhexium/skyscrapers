package net.lab0.skyscrapers.client.shell.spring.data

sealed class ShellResult {
  sealed class Ok: ShellResult() {
    data class Text(val output: String): Ok()
  }

  sealed class Problem: ShellResult() {
    data class Text(val output: String): Problem()
  }
}
