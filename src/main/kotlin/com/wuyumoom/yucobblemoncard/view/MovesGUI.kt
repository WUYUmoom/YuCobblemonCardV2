package com.wuyumoom.yucobblemoncard.view

import com.cobblemon.mod.common.api.moves.MoveTemplate
import com.cobblemon.mod.common.api.moves.Moves
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.model.MovesCard
import com.wuyumoom.yucobblemoncard.model.MovesState.*
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.pokemon.PokemonAPI
import com.wuyumoom.yucore.api.pokemon.base.YuMove
import com.wuyumoom.yucore.api.pokemon.base.YuSprite
import com.wuyumoom.yucore.file.view.Button
import com.wuyumoom.yucore.file.view.ViewConfiguration
import com.wuyumoom.yucore.view.AbstractUI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class MovesGUI(
    player: Player,
    val configuration: ViewConfiguration,
    val pokemon: Pokemon,
    val item: ItemStack,
    val card: MovesCard,
    var page: Int,
    val int: Int?,
) : AbstractUI(
    configuration.title, player, configuration.size
) {
    override fun draw() {
        inventory.clear()
        configuration.button.forEach {
            if (it.key == "Pixel" || it.key == "Moves" || it.key == "PokeMoves") {
                return@forEach
            }
            setItem(it.value.slot, it.value.itemStack)
        }
        val pixel = configuration.button["Pixel"] ?: return
        setItem(pixel.slot, ItemStackAPI.onSetItemMeta(YuSprite.getSpriteItem(pokemon), pixel, pokemon))
        val button = configuration.button["Moves"] ?: return
        val size = button.slot.size * page
        var index = -1
        if (int != null) {
            val moveList = mutableListOf<MoveTemplate>()
            card.move.forEach { it ->
                when (it) {
                    EGG -> {
                        moveList.addAll(pokemon.species.moves.eggMoves)
                    }

                    TM -> {
                        moveList.addAll(pokemon.species.moves.tmMoves)
                    }

                    TUTOR -> {
                        moveList.addAll(pokemon.species.moves.tutorMoves)
                    }

                    LEVEL -> {
                        pokemon.species.moves.levelUpMoves.values.forEach {
                            it.forEach { move -> moveList.add(move) }
                        }
                    }

                    LEGACY -> {
                        moveList.addAll(pokemon.species.moves.legacyMoves)
                    }

                    SPECIAL -> {
                        moveList.addAll(pokemon.species.moves.specialMoves)
                    }

                    EVOLUTION -> {
                        moveList.addAll(pokemon.species.moves.evolutionMoves)
                    }

                    FORM -> {
                        moveList.addAll(pokemon.species.moves.formChangeMoves)
                    }
                }
            }
            button.slot.forEach { slot ->
                index++
                if (index + size >= moveList.size) {
                    return
                }
                val moves = moveList[index + size]
                inventory.setItem(slot, setMoveItem(button.itemStack.clone(), moves,button))
            }
        } else {
            val button = configuration.button["PokeMoves"] ?: return
            pokemon.moveSet.forEach {
                index++
                val nbt =
                    ItemStackAPI.setNBT(setMoveItem(button.itemStack.clone(), it.template,button), "slot", index.toString())
                inventory.setItem(button.slot[index], nbt)
            }
        }
    }

    private fun setMoveItem(item: ItemStack, move: MoveTemplate,button: Button): ItemStack {
        val clone = item.clone()
        clone.type =getMaterial(move,button)
        val itemMeta = clone.itemMeta ?: return clone
        val onGetTranslatePath = PokemonAPI.onGetTranslatePath(move.displayName)
        itemMeta.setDisplayName(itemMeta.displayName.replace("%Moves%", onGetTranslatePath))
        itemMeta.lore = itemMeta.lore?.map {
            it.replace("%Moves%", onGetTranslatePath)
        }
        clone.itemMeta = itemMeta
        ItemStackAPI.setNBT(clone, "Moves", move.name)
        return clone
    }

    private fun getMaterial(move: MoveTemplate,button: Button):Material {
        val id = button.id.replace("%Moves%", move.name).uppercase()
        val material = Material.getMaterial(id)
        return material ?: ConfigManager.movesGUIDefaultID
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
                MovesGUI(player, configuration, pokemon, item, card, page - 1, this.int).openInventory(player)
                return
            }

            "下一页" -> {
                MovesGUI(player, configuration, pokemon, item, card, page + 1, this.int).openInventory(player)
                return
            }

            "Moves" -> {
                val nbt1 = ItemStackAPI.getNBT(currentItem, "slot")
                if (nbt1 == null || nbt1.isEmpty()) {
                    val movesName = ItemStackAPI.getNBT(currentItem, "Moves")
                    this.int?.let { pokemon.moveSet.setMove(it, YuMove.getMove(movesName)) }
                    item.amount--
                    closeInventory()
                    ConfigManager.message.sendMessage("use", player)
                } else {
                    MovesGUI(player, configuration, pokemon, item, card, page + 1, nbt1.toInt()).openInventory(player)
                }
            }
        }
    }
}