package com.wuyumoom.yucobblemoncard.view

import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.model.AddState.*
import com.wuyumoom.yucobblemoncard.model.HyperTrainCard
import com.wuyumoom.yucobblemoncard.model.PokeState
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.pokemon.base.YuStats
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import com.wuyumoom.yucore.file.view.ViewConfiguration
import com.wuyumoom.yucore.view.AbstractUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HyperTrainGUI(
    player: Player,
    val configuration: ViewConfiguration,
    val item: ItemStack,
    val card: HyperTrainCard
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
        val nbt = ItemStackAPI.getNBT(currentItem, "yubutton") ?: return
        val stat: Stat = YuStats.getStats()[nbt] ?: return
        val serverPlayer = player.uniqueId.getPlayer() ?: return
        serverPlayer.openPartyWithCallback(
            hoverText = ConfigManager.tip,
            enabled = { pokemon ->
                (card.isUse(pokemon) && (isStat(stat, pokemon)))
            }
        ) { poke ->
            when (card.addState) {
                ADD -> {
                    val i = poke.ivs.hyperTrainedIVs[stat] ?: 0
                    poke.hyperTrainIV( stat, card.value+i)
                }
                INI -> {
                    poke.hyperTrainIV( stat, card.value)
                }
            }
            item.amount--
            ConfigManager.message.sendMessage("use", player)
        }
    }

    /**
     * 判断个体是否满足
     */
    private fun isStat(stat: Stat, pokemon: Pokemon): Boolean {
        val i = pokemon.ivs.hyperTrainedIVs[stat] ?: 0
        return 31 > i

    }

}