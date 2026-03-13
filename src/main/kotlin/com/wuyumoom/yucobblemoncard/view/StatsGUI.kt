package com.wuyumoom.yucobblemoncard.view

import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.model.PokeState
import com.wuyumoom.yucobblemoncard.model.StateCard
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.pokemon.base.YuStats
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import com.wuyumoom.yucore.file.view.ViewConfiguration
import com.wuyumoom.yucore.view.AbstractUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class StatsGUI (
    player: Player,
    val configuration: ViewConfiguration,
    val item: ItemStack,
    val card: StateCard
) : AbstractUI(
    configuration.title, player, configuration.size
) {
    override fun draw() {
        inventory.clear()
        configuration.button.forEach {
            setItem(it.value.slot, it.value.itemStack)
        }
    }
    private fun setItem(slot: IntArray, itemStack: ItemStack) {
        slot.forEach { slot ->
            inventory.setItem(slot, itemStack)
        }
    }

    override fun openInventory(player: Player) {
        draw()
        player.openInventory(inventory)
    }

    override fun closeInventory() {
        inventory.clear()
        player.closeInventory()
    }

    override fun onClick(int: Int, event: InventoryClickEvent) {
        event.isCancelled = true
        val currentItem = event.currentItem ?: return
        val nbt = ItemStackAPI.getNBT(currentItem, "yubutton")?: return
        val stat: Stat = YuStats.getStats()[nbt]?: return
        val serverPlayer = player.uniqueId.getPlayer()?:return
        serverPlayer.openPartyWithCallback(
            hoverText = ConfigManager.tip,
            enabled = { pokemon ->
                (card.isUse(pokemon) && (isStat(stat,pokemon)))
            }
        ) { poke->
            if (card.isAdd){
                if (card.pokeState == PokeState.IVS){
                    poke.setIV(stat,31)
                }else{
                    poke.setEV(stat,252)
                }
            }else{
                if (card.pokeState == PokeState.IVS){
                    poke.setIV(stat,0)
                }else{
                    poke.setEV(stat,0)
                }
            }
            item.amount--
            ConfigManager.message.sendMessage("use",player)
        }
    }

    /**
     * 判断个体是否满足
     */
    private fun isStat(stat: Stat,pokemon: Pokemon): Boolean{
        return if (card.isAdd){
            if (card.pokeState == PokeState.IVS){
                pokemon.ivs.getOrDefault( stat) < 31
            }else{
                pokemon.evs.getOrDefault( stat) < 252 && getIsEvs(pokemon)
            }
        }else{
            if (card.pokeState == PokeState.IVS){
                pokemon.ivs.getOrDefault( stat) > 0
            }else{
                pokemon.evs.getOrDefault( stat) > 0
            }
        }
    }
    private fun getIsEvs(pokemon: Pokemon): Boolean{
        var i = 0
        YuStats.getStats().values.forEach {
            val orDefault = pokemon.evs.getOrDefault(it)
            if (orDefault >= 252){
                i++
            }
        }
        return i < 2
    }

}