package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.view.GenderGUI
import com.wuyumoom.yucobblemoncard.view.GrowthGUI
import com.wuyumoom.yucobblemongrowth.config.ConfigManager.setGrowth
import com.wuyumoom.yucore.api.pokemon.PokemonLabel
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GrowthCard (
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>,
    val isRandom: Boolean
) : Card(type,name, item, filter, poke) {
    override fun execute(player: Player, item: ItemStack) {
        val serverPlayer = player.uniqueId.getPlayer()?:return
        serverPlayer.openPartyWithCallback(
            hoverText = ConfigManager.tip,
            enabled = { pokemon -> isUse(pokemon) }
        ) { poke->
            if (isRandom){
                val instance = PokemonLabel.getInstance(poke)
                instance.removeLabel("YuGrowth@${instance.getLabelContainsBoolean("YuGrowth")}")
                setGrowth(poke)
                item.amount--
                ConfigManager.message.sendMessage("use",player)
            }else{
                val configuration = ConfigManager.viewConfigurationMap["GrowthGUI"]?:return@openPartyWithCallback
                GrowthGUI(player, configuration, poke,item,0).openInventory(player)
            }
        }
    }
}