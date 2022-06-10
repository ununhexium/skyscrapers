package net.lab0.skyscrapers.client.shell.spring.component

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.jline.PromptProvider
import org.springframework.stereotype.Component


@Component
class SkyscrapersPrompt(val gameMaster: GameMaster) : PromptProvider {

  override fun getPrompt(): AttributedString {
    return if (gameMaster.isConnected()) {
      if (gameMaster.inGame) {
        AttributedString(
          "${gameMaster.state.currentGame?.value}:>",
          AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)
        )
      } else {
        AttributedString(
          "sky\u26A1:>",
          AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)
        )
      }
    } else {
      AttributedString(
        "sky:>",
        AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)
      )
    }
  }

}

