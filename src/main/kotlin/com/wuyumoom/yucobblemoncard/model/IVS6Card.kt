package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucore.api.pokemon.PokemonAPI
import com.wuyumoom.yucore.api.pokemon.base.YuStats
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IVS6Card(
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>
): Card(type,name, item, filter, poke) {
    override fun execute(player: Player, item: ItemStack) {
        val serverPlayer = player.uniqueId.getPlayer()?:return
        serverPlayer.openPartyWithCallback(
            hoverText = ConfigManager.tip,
            enabled = { pokemon ->
                (isUse(pokemon) && PokemonAPI.isIvs(pokemon) < 6)
            }
        ) { poke->
            YuStats.getStats().forEach { stats->
                poke.setIV(stats.value, 31)
            }
            item.amount--
            ConfigManager.message.sendMessage("use",player)
        }
    }
}