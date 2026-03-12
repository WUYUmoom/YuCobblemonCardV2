package com.wuyumoom.yucobblemoncard

import com.wuyumoom.yucobblemoncard.cmd.Commands
import com.wuyumoom.yucobblemoncard.config.ConfigManager
import com.wuyumoom.yucobblemoncard.listener.PluginEvent
import com.wuyumoom.yucore.api.pokemon.base.YuPokeBall
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class YuCobblemonCard : JavaPlugin (){
    companion object {
        lateinit var pluginFile: File
        lateinit var INSTANCE: YuCobblemonCard
        val LOGO = arrayOf(
            "===============================================================================",
            "§f██    ██ ██    ██  ██████  ██████  ██████  ██████  ██      ███████ ███    ███  ██████  ███    ██  ██████  █████  ██████  ██████ ",
            "§f ██  ██  ██    ██ ██      ██    ██ ██   ██ ██   ██ ██      ██      ████  ████ ██    ██ ████   ██ ██      ██   ██ ██   ██ ██   ██ ",
            "§f  ████   ██    ██ ██      ██    ██ ██████  ██████  ██      █████   ██ ████ ██ ██    ██ ██ ██  ██ ██      ███████ ██████  ██   ██ ",
            "§f   ██    ██    ██ ██      ██    ██ ██   ██ ██   ██ ██      ██      ██  ██  ██ ██    ██ ██  ██ ██ ██      ██   ██ ██   ██ ██   ██ ",
            "§f   ██     ██████   ██████  ██████  ██████  ██████  ███████ ███████ ██      ██  ██████  ██   ████  ██████ ██   ██ ██   ██ ██████  ",
            "§e§l语之方可梦卡片 §6§l启动完成！",
            "§e§l作者 : 姬无语 §6§lQQ1841375451",
            "==============================================================================="
        )
    }
    override fun onEnable() {
        INSTANCE = this
        pluginFile = this.file
        saveDefaultConfig()
        ConfigManager.load()
        getCommand("ycard")?.let {
            it.setExecutor(Commands)
            it.tabCompleter = Commands
        }
        Bukkit.getPluginManager().registerEvents(PluginEvent, this)
        Bukkit.getConsoleSender().sendMessage(*LOGO)
    }
    override fun onDisable() {
    }
}