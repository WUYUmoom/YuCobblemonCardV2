package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.view.NatureGUI
import com.wuyumoom.yucobblemoncard.view.StatsGUI
import com.wuyumoom.yucore.api.pokemon.base.YuNature
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class NatureCard (
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>,
    val isRandom: Boolean
): Card(type,name, item, filter, poke) {
    override fun execute(player: Player, item: ItemStack) {
        val serverPlayer = player.uniqueId.getPlayer()?:return
        serverPlayer.openPartyWithCallback(
            hoverText = ConfigManager.tip,
            enabled = { pokemon -> isUse(pokemon) }
        ) { poke->
            if (isRandom){
                val random = YuNature.getNature().values.random()
                poke.nature = random
                item.amount--
                ConfigManager.message.sendMessage("use",player)
            }else{
                val configuration = ConfigManager.viewConfigurationMap["NatureGUI"]?: return@openPartyWithCallback
                NatureGUI(player, configuration, item,this, poke).openInventory(player)
            }
        }
    }

}