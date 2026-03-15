package com.wuyumoom.yucobblemoncard.view

import com.cobblemon.mod.common.pokemon.Gender
import com.cobblemon.mod.common.pokemon.Pokemon
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.pokemon.base.YuSprite
import com.wuyumoom.yucore.file.view.ViewConfiguration
import com.wuyumoom.yucore.view.AbstractUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class GenderGUI(
    player: Player,
    val configuration: ViewConfiguration,
    val pokemon: Pokemon,
    val item: ItemStack
) : AbstractUI(
    configuration.title, player, configuration.size
) {
    override fun draw() {
        inventory.clear()
        for (entry in configuration.button) {
            if (entry.key == "Pixel"){
                continue
            }
            setItem(entry.value.slot, entry.value.itemStack)
        }
        val button = configuration.button["Pixel"]?: return
        setItem(button.slot,ItemStackAPI.onSetItemMeta(YuSprite.getSpriteItem(pokemon),button, pokemon))
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
        val nbt = ItemStackAPI.getNBT(currentItem, "yubutton")
        when (nbt) {
            "性别雌"->{
                pokemon.gender = Gender.FEMALE
                ConfigManager.message.sendMessage("use", player)
                item.amount--
                closeInventory()
                return
            }
            "性别雄"->{
                pokemon.gender = Gender.MALE
                ConfigManager.message.sendMessage("use", player)
                closeInventory()
                item.amount--
                return
            }
        }
    }

}