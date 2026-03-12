package com.wuyumoom.yucobblemoncard.api.card

import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import net.minecraft.server.level.ServerPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


/**
 * 筛选模式
 */
enum class CardType {
    SYSTEM,      // 系统卡片（如性别卡、6V 卡、闪光卡等）
    MOVE,       // 技能卡片（自定义技能卡）
    CUSTOM,       // 自定义卡片（用户自定义功能卡）
    POKEMON

}
/**
 * 筛选模式
 */
enum class UseState {
    WHITE,
    BLACK,

}
abstract class Card(
    var type: CardType,
    var name: String,
    var item: ItemStack,
    var filter: UseState,
    var poke: MutableList<Species>
) {

    /**
     * 判断是否使用
     */
    fun isUse(pokemon: Pokemon): Boolean {
        return when (filter) {
            UseState.WHITE -> poke.contains(pokemon.species)
            UseState.BLACK -> !poke.contains(pokemon.species)
        }
    }


    /**
     * 实现方法
     */
    abstract fun execute(player: Player,item: ItemStack)
}