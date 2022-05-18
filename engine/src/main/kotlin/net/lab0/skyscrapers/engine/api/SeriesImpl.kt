package net.lab0.skyscrapers.engine.api

class SeriesImpl constructor(override val requiredWinningRounds: Int) : Series {

  private val games = mutableListOf<Game>()
  private var gamePointer: Int? = null

  override fun withGame(f: Game.() -> Unit): Boolean {
    val game = games.lastOrNull()
      ?: return false

    f(game)
    if(game.state.isFinished()) gamePointer = null

    return true
  }

  override fun start() {
    if (isFinished()) throw IllegalStateException("Can't start a new game when the series was won: the winner is player #$winner")

    if (gamePointer != null) throw IllegalStateException(
      "Can't start a new game as the current one is not finished. It's time for player #${games.last().state.currentPlayer} to play."
    )

    games.add(Game.new())
    gamePointer = games.lastIndex
  }

  override fun getRound(index: Int) =
    games.getOrNull(index)

  override val rounds
    get() = games.size

  override val currentRound
    get() = gamePointer

  override fun isFinished() =
    games
      .groupingBy { it.state.winner }
      .eachCount()
      .values
      .any { it >= requiredWinningRounds }

  override val winner
    get() =
      if (isFinished()) {
        games
          .groupingBy { it.state.winner }
          .eachCount()
          .entries
          .maxByOrNull { it.value }
          ?.key
      } else null
}
