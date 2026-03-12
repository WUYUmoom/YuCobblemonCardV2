package com.wuyumoom.yucobblemoncard.view

import com.cobblemon.mod.common.pokeball.PokeBall
import com.cobblemon.mod.common.pokemon.Nature
import com.cobblemon.mod.common.pokemon.Pokemon
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.model.NatureCard
import com.wuyumoom.yucobblemoncard.model.PokeBallCard
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.NMS
import com.wuyumoom.yucore.api.pokemon.PokemonAPI
import com.wuyumoom.yucore.api.pokemon.base.YuNature
import com.wuyumoom.yucore.api.pokemon.base.YuPokeBall
import com.wuyumoom.yucore.api.pokemon.base.YuSprite
import com.wuyumoom.yucore.file.view.ViewConfiguration
import com.wuyumoom.yucore.view.AbstractUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import kotlin.text.replace

class PokeBallGUI (
    player: Player,
    val configuration: ViewConfiguration,
    val item: ItemStack,
    val card: PokeBallCard,
    var pokemon: Pokemon,
    var page: Int,
) : AbstractUI(
    configuration.title, player, configuration.size
) {
    override fun draw() {
        inventory.clear()
        configuration.button.forEach {
            if (it.key == "PokeBall"||it.key == "Pixel"){
                return@forEach
            }
            setItem(it.value.slot, it.value.itemStack)
        }
        val pixel = configuration.button["Pixel"] ?: return
        setItem(pixel.slot, ItemStackAPI.onSetItemMeta(YuSprite.getSpriteItem(pokemon), pixel, pokemon))
        val button = configuration.button["PokeBall"] ?: return
        var index = -1
        val size = button.slot.size * page
        val values = YuPokeBall.getPokeBall().values.toMutableList()
        button.slot.forEach { slot ->
            index++
            if (index + size >= values.size) {
                return
            }
            val pokeBall = values[index + size]
            val mnsFaItemStack = NMS.getMNSFaItemStack(pokeBall.item().defaultInstance)
            val itemStack = button.itemStack.clone()
            itemStack.type = mnsFaItemStack.type
            inventory.setItem(button.slot[index],setPokeBallItem(itemStack, PokemonAPI.onGetTranslatePath(pokeBall.stack(1).hoverName)))
        }
    }
    private fun setPokeBallItem(item: ItemStack, nature: String): ItemStack{
        val clone = item.clone()
        val itemMeta = clone.itemMeta ?: return clone
        itemMeta.setDisplayName(itemMeta.displayName.replace("%PokeBall%",nature ))
        itemMeta.lore = itemMeta.lore?.map {
            it.replace("%PokeBall%",nature)
        }
        clone.itemMeta = itemMeta
        return ItemStackAPI.setNBT(clone, "pokeball", nature)
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
            "上一页" -> {
                if (page <= 0) {
                    return
                }
                PokeBallGUI(player, configuration,item, card,pokemon, page - 1).openInventory(player)
                return
            }

            "下一页" -> {
                PokeBallGUI(player, configuration,item, card,pokemon, page + 1).openInventory(player)
                return
            }
            "PokeBall"->{
                val ball = ItemStackAPI.getNBT(currentItem, "pokeball")?: return
                val pokeBall: PokeBall = YuPokeBall.getPokeBall(ball)?: return
                closeInventory()
                pokemon.caughtBall = pokeBall
                item.amount--
                ConfigManager.message.sendMessage("use",player)
            }
        }
    }


}