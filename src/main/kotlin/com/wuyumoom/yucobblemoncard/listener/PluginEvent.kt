package com.wuyumoom.yucobblemoncard.listener

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.block.PastureBlock
import com.cobblemon.mod.common.platform.events.PlatformEvents
import com.cobblemon.mod.common.util.getPlayer
import com.wuyumoom.yucobblemoncard.YuCobblemonCard
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.NMS
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object PluginEvent : Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item: ItemStack = event.item ?: return
        if (!event.hasItem()) return
        if (event.hand == EquipmentSlot.HAND){
            val nbt = ItemStackAPI.getNBT(item, "yuitem") ?: return
            val card = ConfigManager.card[nbt] ?: return
            card.execute(player,item)
        }

    }



}