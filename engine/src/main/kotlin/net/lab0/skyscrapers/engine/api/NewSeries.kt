package net.lab0.skyscrapers.engine.api

interface NewSeries {
  fun newBestOf1() = SeriesImpl(1)
  fun newBestOf3() = SeriesImpl(3)
  fun newBestOfX(x: Int, gameFactory: () -> Game = { Game.new() }) =
    SeriesImpl(x, gameFactory)
}
