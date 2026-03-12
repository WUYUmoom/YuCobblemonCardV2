package com.wuyumoom.yucobblemoncard.view

import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.pokemon.Nature
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.model.NatureCard
import com.wuyumoom.yucobblemoncard.model.PokeState
import com.wuyumoom.yucobblemoncard.model.StateCard
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.pokemon.base.YuNature
import com.wuyumoom.yucore.api.pokemon.base.YuSprite
import com.wuyumoom.yucore.api.pokemon.base.YuStats
import com.wuyumoom.yucore.api.pokemon.openPartyWithCallback
import com.wuyumoom.yucore.file.view.ViewConfiguration
import com.wuyumoom.yucore.view.AbstractUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class NatureGUI(
    player: Player,
    val configuration: ViewConfiguration,
    val item: ItemStack,
    val card: NatureCard,
    var pokemon: Pokemon
) : AbstractUI(
    configuration.title, player, configuration.size
) {
    override fun draw() {
        inventory.clear()
        configuration.button.forEach {
            if (it.key == "Nature"||it.key == "Pixel"){
                return@forEach
            }
            setItem(it.value.slot, it.value.itemStack)
        }
        val button = configuration.button["Nature"] ?: return
        var index = -1
        YuNature.getNature().forEach { nature ->
            index++
            inventory.setItem(button.slot[index],setNatureItem(button.itemStack,nature.key))
        }
        val pixel = configuration.button["Pixel"] ?: return
        setItem(pixel.slot, ItemStackAPI.onSetItemMeta(YuSprite.getSpriteItem(pokemon), pixel, pokemon))
    }
    private fun setNatureItem(item: ItemStack, nature: String): ItemStack{
        val clone = item.clone()
        val itemMeta = clone.itemMeta ?: return clone
        itemMeta.setDisplayName(itemMeta.displayName.replace("%Nature%",nature ))
        itemMeta.lore = itemMeta.lore?.map {
            it.replace("%Nature%",nature)
        }
        clone.itemMeta = itemMeta
       return ItemStackAPI.setNBT(clone, "nature", nature)
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
        val nbt = ItemStackAPI.getNBT(currentItem, "nature")?: return
        val nature: Nature = YuNature.getNature(nbt)?: return
        closeInventory()
        pokemon.nature = nature
        item.amount--
        ConfigManager.message.sendMessage("use",player)
    }


}