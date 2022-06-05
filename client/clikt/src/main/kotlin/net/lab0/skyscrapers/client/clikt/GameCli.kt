package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.lab0.skyscrapers.client.clikt.command.Configuration
import net.lab0.skyscrapers.client.clikt.command.Connect
import net.lab0.skyscrapers.client.clikt.command.Current
import net.lab0.skyscrapers.client.clikt.command.Join
import net.lab0.skyscrapers.client.clikt.command.NewGame
import net.lab0.skyscrapers.client.clikt.command.Place
import net.lab0.skyscrapers.client.clikt.command.Show
import org.koin.core.component.KoinComponent
import java.io.Writer

class GameCli : CliktCommand() {
  companion object : KoinComponent {
    fun new(writer: Writer?): CliktCommand {
      return GameCli().subcommands(
        // TODO: list available games
        Connect(writer),
        Configuration(writer),
        NewGame(writer),
        Show(writer),
        Join(writer),
        Current(writer),
        Place(writer),
      )
    }
  }

  override fun run() = Unit
}
