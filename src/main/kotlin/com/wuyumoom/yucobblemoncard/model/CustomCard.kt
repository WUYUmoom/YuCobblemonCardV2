package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucore.api.BukkitAPI
import com.wuyumoom.yucore.api.pokemon.PokemonAPI
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 自定义卡
 */
class CustomCard(
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>,
    val function: String
):Card(type,name, item, filter, poke) {

    override fun execute(player: Player,item: ItemStack) {
        val serverPlayer = player.uniqueId.getPlayer()?:return
        serverPlayer.openPartyWithCallback(
            hoverText = ConfigManager.tip,
            enabled = { pokemon -> isUse(pokemon) }
        ) { poke->
            val onSetString = BukkitAPI.onSetString(function, " ")
            PokemonAPI.onSetForPokemon(poke, onSetString)
            item.amount--
            ConfigManager.message.sendMessage("use",player)
        }
    }
}