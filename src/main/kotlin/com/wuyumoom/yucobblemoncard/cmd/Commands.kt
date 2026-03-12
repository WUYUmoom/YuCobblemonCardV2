package com.wuyumoom.yucobblemoncard.cmd

import com.wuyumoom.yucobblemoncard.config.ConfigManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object Commands : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String> {
        val subCommand: MutableList<String> = mutableListOf()
        return when (args.size) {
            1 -> {
                subCommand.add("help")
                if (sender.isOp) {
                    subCommand.add("give")
                    subCommand.add("reload")
                }
                subCommand.filter { it.startsWith(args[0], ignoreCase = true) }
            }
            2 -> {
                if (args[0] == "give") {
                    subCommand.addAll(ConfigManager.card.keys.filter { it.startsWith(args[1], ignoreCase = true) })
                }
                subCommand
            }
            3 -> {
                if (args[0] == "give") {
                    subCommand.addAll(Bukkit.getOnlinePlayers().map { it.name })
                }
                subCommand
            }
            4 -> {
                if (args[0] == "give") {
                    // 第四个参数（give 命令）：数量（默认为 1）
                    val amounts = listOf("1", "5", "10", "32", "64")
                    return amounts.filter { it.contains(args[3]) }
                }
                subCommand
            }
            else -> emptyList()
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (args.isEmpty()){
            showHelp(sender)
            return true
        }
        when(args[0]){
            "give"->{
                if (!sender.isOp){
                    sender.sendMessage("§c你没有权限使用此命令")
                    return true
                }
                if (args.size < 3){
                    sender.sendMessage("§c请输入正确的参数")
                    return true
                }
                val player = Bukkit.getPlayer(args[2])
                if (player == null|| !player.isOnline){
                    sender.sendMessage("§c未找到玩家")
                    return true
                }
                val card = ConfigManager.card[args[1]]
                if (card == null){
                    sender.sendMessage("§c未找到卡片")
                    return true
                }
                var amount = 1
                if (args.size > 3){
                    amount = args[3].toIntOrNull() ?: 1
                }
                val clone = card.item.clone()
                clone.amount = amount
                player.inventory.addItem(clone)
                return true
            }
            "reload"->{
                if (!sender.isOp){
                    sender.sendMessage("§c你没有权限使用此命令")
                    return  true
                }
                ConfigManager.reload()
                sender.sendMessage("§a插件配置已重载")
            }
            "help"->{
                showHelp(sender)
            }
        }
        return true
    }
    /**
     * 显示帮助信息（根据权限过滤）
     */
    private fun showHelp(sender: CommandSender) {
        sender.sendMessage("§6===== §e语之方可梦卡片 §6=====")
        sender.sendMessage("§7使用方法: §f/ycard <子命令>")
        if (sender.isOp) {
            sender.sendMessage("§f/ycard give <卡片> <玩家> [数量] §7- 给与玩家卡片")
            sender.sendMessage("§f/ycard reload §7- 重载插件配置")
        }
        sender.sendMessage("§f/ycard help §7- 显示此帮助信息")
        sender.sendMessage("§6========================")
    }

}