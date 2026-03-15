package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.pokemon.Gender
import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.view.GenderGUI
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 性别卡
 */
class GenderCard(
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>
) : Card(type,name, item, filter, poke) {
    override fun execute(player: Player,item: ItemStack) {
        val serverPlayer = player.uniqueId.getPlayer()?:return
        serverPlayer.openPartyWithCallback(
            hoverText = ConfigManager.tip,
            enabled = { pokemon -> (isUse(pokemon)&& pokemon.gender != Gender.GENDERLESS) }
        ) { poke->
            val configuration = ConfigManager.viewConfigurationMap["GenderGUI"]?:return@openPartyWithCallback
            GenderGUI(player, configuration, poke,item).openInventory(player)
        }
    }
}