package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.pokemon.Species
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.view.HyperTrainGUI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class HyperTrainCard (
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>,
    val addState: AddState,
    val value: Int
): Card(type,name, item, filter, poke) {
    override fun execute(player: Player, item: ItemStack) {
        val configuration = ConfigManager.viewConfigurationMap["StatsGUI"]?:return
        HyperTrainGUI(player, configuration, item,this).openInventory(player)
    }

}