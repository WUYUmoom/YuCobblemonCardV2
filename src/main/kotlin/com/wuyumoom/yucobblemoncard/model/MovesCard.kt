package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.view.GenderGUI
import com.wuyumoom.yucobblemoncard.view.MovesGUI
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
enum class MovesState {
    EGG,
    TM,
    TUTOR,
    LEVEL,
    LEGACY,
    SPECIAL,
    EVOLUTION,
    FORM,
}

class MovesCard (
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>,
    val move: MutableList<MovesState>
): Card(type,name, item, filter, poke) {
    override fun execute(player: Player, item: ItemStack) {
        val serverPlayer = player.uniqueId.getPlayer()?:return
        serverPlayer.openPartyWithCallback(
            hoverText = ConfigManager.tip,
            enabled = { pokemon -> isUse(pokemon) }
        ) { poke->
            val configuration = ConfigManager.viewConfigurationMap["MovesGUI"]?:return@openPartyWithCallback
            MovesGUI(player, configuration, poke,item, this, 0, null).openInventory(player)
        }
    }
}