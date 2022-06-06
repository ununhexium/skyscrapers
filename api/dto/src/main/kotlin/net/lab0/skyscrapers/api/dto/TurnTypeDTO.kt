package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.TurnType

// TODO: split that into 5 API endpoints -> 5 DTOs
@Serializable
class TurnTypeDTO private constructor(
  val giveUp: GiveUpTurnDTO? = null,
  val place: PlaceTurnDTO? = null,
  val build: BuildTurnDTO? = null,
  val seal: SealTurnDTO? = null,
  val win: WinTurnDTO? = null,
) {
  companion object {
    fun giveUp(giveUp: GiveUpTurnDTO) = TurnTypeDTO(giveUp = giveUp)
    fun place(place: PlaceTurnDTO) = TurnTypeDTO(place = place)
    fun build(build: BuildTurnDTO) = TurnTypeDTO(build = build)
    fun seal(seal: SealTurnDTO) = TurnTypeDTO(seal = seal)
    fun win(win: WinTurnDTO) = TurnTypeDTO(win = win)
  }

  fun toTurnType(): TurnType {
    var nonNulls = 0
    if (giveUp != null) nonNulls++
    if (place != null) nonNulls++
    if (build != null) nonNulls++
    if (seal != null) nonNulls++
    if (win != null) nonNulls++

    if (nonNulls > 1) throw IllegalStateException("The turn type may only be of 1 kind. The other kinds must be null.")

    if (giveUp != null)
      return TurnType.GiveUpTurn(giveUp.player)

    throw IllegalStateException("There must be at least 1 type of turn that is not null.")
  }
}