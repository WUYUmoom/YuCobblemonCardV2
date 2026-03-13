package com.wuyumoom.yucobblemoncard.view

import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Pokemon
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.pokemon.base.YuSprite
import com.wuyumoom.yucore.file.view.ViewConfiguration
import com.wuyumoom.yucore.view.AbstractUI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class FormsGUI (
    player: Player,
    val configuration: ViewConfiguration,
    val pokemon: Pokemon,
    val item: ItemStack,
) : AbstractUI(
    configuration.title, player, configuration.size
) {
    override fun draw() {
        inventory.clear()
        configuration.button.forEach {
            if (it.key == "Pixel" || it.key == "Forms"){
                return@forEach
            }
            setItem(it.value.slot, it.value.itemStack)
        }
        val pixel = configuration.button["Pixel"]?: return
        setItem(pixel.slot,ItemStackAPI.onSetItemMeta(YuSprite.getSpriteItem(pokemon),pixel, pokemon))
        val button = configuration.button["Forms"] ?: return
        var index = -1
        pokemon.species.forms.forEach {
            index++
            val create = it.species.create(10)
            create.form = it
            create.forcedAspects = it.aspects.toSet()
            create.updateAspects()
            create.updateForm()
            val spriteItem = YuSprite.getSpriteItem(create)
            val onSetItemMeta = ItemStackAPI.onSetItemMeta(spriteItem, button)
            inventory.setItem(button.slot[index],setFormsItem(onSetItemMeta, it))
        }
    }

    private fun setFormsItem(item: ItemStack,forms:FormData) : ItemStack{
        val clone = item.clone()
        val itemMeta = clone.itemMeta ?: return clone
        itemMeta.setDisplayName(itemMeta.displayName.replace("%Forms%",forms.name ))
        itemMeta.lore = itemMeta.lore?.map {
            it.replace("%Forms%",forms.name)
        }
        clone.itemMeta = itemMeta
        return ItemStackAPI.setNBT(clone, "forms", forms.formOnlyShowdownId())
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
        val formsNBT = ItemStackAPI.getNBT(currentItem, "forms")
        if (!formsNBT.isEmpty()){
            val formByShowdownId = pokemon.species.getFormByShowdownId(formsNBT)
            pokemon.form = formByShowdownId
            pokemon.forcedAspects = formByShowdownId.aspects.toSet()
            pokemon.updateAspects()
            pokemon.updateForm()
            item.amount--
            ConfigManager.message.sendMessage("use",player)
            closeInventory()
        }
    }


}