package net.lab0.skyscrapers.engine.api

import net.lab0.skyscrapers.engine.GameFactoryImpl

interface NewSeries {
  fun newBestOf1() = SeriesImpl(1)
  fun newBestOf3() = SeriesImpl(3)
  fun newBestOfX(x: Int, gameFactory: () -> Game = { GameFactoryImpl().new() }) =
    SeriesImpl(x, gameFactory)
}
