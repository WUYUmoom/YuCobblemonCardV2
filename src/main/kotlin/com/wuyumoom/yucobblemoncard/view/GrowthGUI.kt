package com.wuyumoom.yucobblemoncard.view

import com.cobblemon.mod.common.pokemon.Pokemon
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemongrowth.config.ConfigManager.privateGrowth
import com.wuyumoom.yucobblemongrowth.config.ConfigManager.publicGrowth
import com.wuyumoom.yucobblemongrowth.growth.Growth
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.pokemon.PokemonAPI
import com.wuyumoom.yucore.api.pokemon.PokemonLabel
import com.wuyumoom.yucore.api.pokemon.base.YuSprite
import com.wuyumoom.yucore.file.view.ViewConfiguration
import com.wuyumoom.yucore.view.AbstractUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class GrowthGUI (
    player: Player,
    val configuration: ViewConfiguration,
    val pokemon: Pokemon,
    val item: ItemStack,
    var page: Int,
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
        val pixel = configuration.button["Pixel"]?: return
        setItem(pixel.slot,ItemStackAPI.onSetItemMeta(YuSprite.getSpriteItem(pokemon),pixel, pokemon))
        val button = configuration.button["Growth"] ?: return
        var index = -1
        val size = button.slot.size * page
        val growthList: MutableList<Growth> = mutableListOf()
        val map = privateGrowth[pokemon.species]
        if (map != null){
            map.values.forEach {
                growthList.addAll(it)
            }
        }else{
            publicGrowth.values.forEach { growthList.addAll(it) }
        }
        button.slot.forEach { slot ->
            index++
            if (index + size >= growthList.size) {
                return
            }
            val growth = growthList[index + size]
            inventory.setItem(button.slot[index],setGrowthItem(button.itemStack, growth))
        }
    }
    private fun setGrowthItem(item: ItemStack,growth:Growth): ItemStack{
        val clone = item.clone()
        val itemMeta = clone.itemMeta ?: return clone
        itemMeta.setDisplayName(itemMeta.displayName.replace("%Growth%",growth.name ))
        itemMeta.lore = itemMeta.lore?.map {
            it.replace("%Growth%",growth.name)
        }
        clone.itemMeta = itemMeta
        return ItemStackAPI.setNBT(clone, "growth", growth.name)
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
                GrowthGUI(player, configuration, pokemon, item, page - 1).openInventory(player)
                return
            }

            "下一页" -> {
                GrowthGUI(player, configuration, pokemon, item, page + 1).openInventory(player)
                return
            }
        }
        val growth = ItemStackAPI.getNBT(currentItem, "growth")
        if (growth != null && !growth.isEmpty()){
            val instance = PokemonLabel.getInstance(pokemon)
            instance.removeLabel("YuGrowth@${instance.getLabelContainsBoolean("YuGrowth")}")
            val map =privateGrowth[pokemon.species]
            if (map == null) {
                setGrowth(pokemon, growth, player, publicGrowth)
            } else {
                setGrowth(pokemon, growth, player, map)
            }

        }
    }
    private fun setGrowth(
        pokemon: Pokemon,
        string: String,
        player: Player,
        map: MutableMap<Int, MutableList<Growth>>
    ) {
        map.values.forEach {
            it.forEach {
                if (it.name == string) {
                    pokemon.scaleModifier = it.scale.toFloat()
                    val instance = PokemonLabel.getInstance(pokemon)
                    instance.removeLabel("YuGrowth@${instance.getLabelContainsBoolean("YuGrowth")}")
                    instance.addLabel("YuGrowth@${it.name}")
                    item.amount--
                    ConfigManager.message.sendMessage("use",player)
                    closeInventory()
                    return
                }
            }
        }
    }

}