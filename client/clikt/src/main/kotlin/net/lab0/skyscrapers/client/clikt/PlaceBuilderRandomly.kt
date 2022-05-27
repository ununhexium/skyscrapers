//package net.lab0.skyscrapers.client.clikt
//
//import com.github.ajalt.clikt.core.CliktCommand
//import net.lab0.skyscrapers.engine.action.DSL
//import net.lab0.skyscrapers.engine.api.Series
//import net.lab0.skyscrapers.api.structure.Position
//import kotlin.random.Random
//
//class PlaceBuilderRandomly(val series: Series) : CliktCommand(
//  name = "randomly"
//) {
//
//  override fun run() {
//    series.withGame {
//      play(
//        DSL.player(state.currentPlayer).placement.addBuilder(
//          Position(
//            Random.nextInt(
//              state.bounds.width
//            ),
//            Random.nextInt(
//              state.bounds.height
//            )
//          )
//        )
//      )
//    }
//
//    // TODO: else warn
//  }
//}