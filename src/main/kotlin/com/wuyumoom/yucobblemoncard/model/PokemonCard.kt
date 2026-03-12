package com.wuyumoom.yucobblemoncard.model

import com.cobblemon.mod.common.pokemon.Species
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucore.api.BukkitAPI
import com.wuyumoom.yucore.api.pokemon.PokemonAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PokemonCard (
    type: CardType,
    name: String,
    item: ItemStack,
    filter: UseState,
    poke: MutableList<Species>,
    val function: String
):Card(type,name, item, filter, poke) {

    override fun execute(player: Player,item: ItemStack) {
        val pokemon = poke.random().create(10)
        val onSetString = BukkitAPI.onSetString(function, " ")
        PokemonAPI.onSetForPokemon(pokemon, onSetString)
        PokemonAPI.onGivePokemon(player, pokemon)
        item.amount--
        ConfigManager.message.sendMessage("use",player)
    }
}