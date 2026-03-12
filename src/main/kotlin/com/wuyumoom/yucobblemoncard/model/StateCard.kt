package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.pokemon.Species
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.view.StatsGUI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


enum class PokeState {
    EVS,
    IVS,
}
class StateCard(
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>,
    val pokeState: PokeState,
    val isAdd: Boolean
): Card(type,name, item, filter, poke) {
    override fun execute(player: Player, item: ItemStack) {
        val configuration = ConfigManager.viewConfigurationMap["StatsGUI"]?:return
        StatsGUI(player, configuration, item,this).openInventory(player)
    }
}