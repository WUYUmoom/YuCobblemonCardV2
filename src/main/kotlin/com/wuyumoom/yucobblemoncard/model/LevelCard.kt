package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class LevelCard (
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>,
    val isLevel: Boolean
): Card(type,name, item, filter, poke) {
    override fun execute(player: Player, item: ItemStack) {
        val serverPlayer = player.uniqueId.getPlayer()?:return
        val maxPokemonLevel = Cobblemon.config.maxPokemonLevel
        serverPlayer.openPartyWithCallback(
            hoverText = ConfigManager.tip,
            enabled = { pokemon ->
                (isUse(pokemon) && isLevel(pokemon,maxPokemonLevel))
            }
        ) { poke->
            setLevel(poke,maxPokemonLevel)
            item.amount--
            ConfigManager.message.sendMessage("use",player)
        }
    }
    private fun isLevel(pokemon: Pokemon,level: Int): Boolean{
        return if (isLevel) {
            pokemon.level < level
        } else {
            pokemon.level > 1
        }
    }
    private fun setLevel(pokemon: Pokemon,level: Int){
        if (isLevel){
            pokemon.level = level
        }else{
            pokemon.level = 1
        }
    }

}