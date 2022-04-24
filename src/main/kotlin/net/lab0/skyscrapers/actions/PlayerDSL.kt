package net.lab0.skyscrapers.actions

data class PlayerDSL(val player: Int, val f: AddBuilderDSL.() -> Unit) {
  fun addBuilder(): AddBuilderDSL {
    val dsl = AddBuilderDSL()
    f(dsl)
    return dsl
  }
}