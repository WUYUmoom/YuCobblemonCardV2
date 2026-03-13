package com.wuyumoom.yucobblemoncard.config

import com.cobblemon.mod.common.pokemon.Species
import com.wuyumoom.yucobblemoncard.YuCobblemonCard
import com.wuyumoom.yucobblemoncard.api.card.Card
import com.wuyumoom.yucobblemoncard.api.card.CardType
import com.wuyumoom.yucobblemoncard.api.card.CardType.*
import com.wuyumoom.yucobblemoncard.api.card.UseState
import com.wuyumoom.yucobblemoncard.model.*
import com.wuyumoom.yucore.api.BukkitAPI
import com.wuyumoom.yucore.api.FileAPI
import com.wuyumoom.yucore.api.ItemStackAPI
import com.wuyumoom.yucore.api.Message
import com.wuyumoom.yucore.api.pokemon.base.YuSpecies
import com.wuyumoom.yucore.file.view.ViewConfiguration
import org.bukkit.configuration.file.YamlConfiguration

object ConfigManager {
    var config = YuCobblemonCard.INSTANCE.config
    var tip: MutableList<String> = mutableListOf()
    val card = mutableMapOf<String, Card>()
    val viewConfigurationMap: MutableMap<String, ViewConfiguration> = HashMap()
    var message: Message = Message(config)

    fun load() {
        config.getStringList("tip").map {
            tip.add(BukkitAPI.onReplace(it))
        }
        FileAPI.folderFiles(YuCobblemonCard.INSTANCE, "view", YuCobblemonCard.pluginFile).forEach {
            val replace = it.name.replace(".yml", "")
            val viewConfiguration = ViewConfiguration(YamlConfiguration.loadConfiguration(it))
            viewConfigurationMap[replace] = viewConfiguration
        }
        FileAPI.folderFiles(YuCobblemonCard.INSTANCE, "card", YuCobblemonCard.pluginFile).forEach {
            val loadConfiguration = YamlConfiguration.loadConfiguration(it)
            loadConfiguration.getKeys(false).forEach {
                val configurationSection = loadConfiguration.getConfigurationSection(it)!!
                val cardType = CardType.valueOf((configurationSection.getString("type") ?: "system").uppercase())
                when (cardType){
                    SYSTEM -> {
                        when (it) {
                            "形态卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = FormsCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                )
                                card[it] = newCard
                            }
                            "随机体型卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = GrowthCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isRandom = true,
                                )
                                card[it] = newCard
                            }
                            "任意体型卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = GrowthCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isRandom = false,
                                )
                                card[it] = newCard
                            }
                            "随机球种卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = PokeBallCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isRandom = true,
                                )
                                card[it] = newCard
                            }
                            "任意球种卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = PokeBallCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isRandom = false,
                                )
                                card[it] = newCard
                            }
                            "任意性格卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = NatureCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isRandom = false,
                                )
                                card[it] = newCard
                            }
                            "随机性格卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = NatureCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isRandom = true,
                                )
                                card[it] = newCard
                            }
                            "任意个体值清空卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = StateCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    pokeState = PokeState.IVS,
                                    isAdd= false
                                )
                                card[it] = newCard
                            }
                            "任意努力值清空卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = StateCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    pokeState = PokeState.EVS,
                                    isAdd= false
                                )
                                card[it] = newCard
                            }
                            "任意努力值卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = StateCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    pokeState = PokeState.EVS,
                                    isAdd= true
                                )
                                card[it] = newCard
                            }
                            "任意个体值卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = StateCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    pokeState = PokeState.IVS,
                                    isAdd= true
                                )
                                card[it] = newCard
                            }
                            "6v卡"->{
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = IVS6Card(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                )
                                card[it] = newCard
                            }
                            "解闪光卡" -> {
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = ShinyCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isShinyCard =  false
                                )
                                card[it] = newCard
                            }
                            "闪光卡" -> {
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = ShinyCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isShinyCard =  true
                                )
                                card[it] = newCard
                            }
                            "任意性别卡" -> {
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = GenderCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke
                                )
                                card[it] = newCard
                            }
                            "满级卡" -> {
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = LevelCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isLevel = true
                                )
                                card[it] = newCard
                            }
                            "等级清空卡" -> {
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = LevelCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    isLevel = false
                                )
                                card[it] = newCard
                            }
                        }
                    }
                    MOVE -> {
                        val poke: MutableList<Species> = mutableListOf()
                        configurationSection.getStringList("use.poke").map {
                            poke.add(YuSpecies.getSpecies(it))
                        }
                        val move: MutableList<MovesState> = mutableListOf()
                        configurationSection.getStringList("move").map {
                            move.add(MovesState.valueOf(it.uppercase()))
                        }
                        val newCard = MovesCard(
                            type = cardType,
                            name = it,
                            item = ItemStackAPI.createItem(configurationSection, true),
                            filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                            poke = poke,
                            move = move
                        )
                        card[it] = newCard
                    }
                    CUSTOM -> {
                        val poke: MutableList<Species> = mutableListOf()
                        configurationSection.getStringList("use.poke").map {
                            poke.add(YuSpecies.getSpecies(it))
                        }
                        val newCard = CustomCard(
                            type = cardType,
                            name = it,
                            item = ItemStackAPI.createItem(configurationSection, true),
                            filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                            poke = poke,
                            function = configurationSection.getString("function") ?: ""
                        )
                        card[it] = newCard
                    }
                    POKEMON -> {
                        val poke: MutableList<Species> = mutableListOf()
                        val loadSpecies = YuSpecies.loadSpecies(configurationSection)
                        val banPoke = configurationSection.getConfigurationSection("banPoke")
                        if (banPoke != null){
                            loadSpecies.removeAll(YuSpecies.loadSpecies(banPoke)   )
                        }
                        poke.addAll(loadSpecies)
                        val newCard = PokemonCard(
                            type = cardType,
                            name = it,
                            item = ItemStackAPI.createItem(configurationSection, true),
                            filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "black").uppercase()),
                            poke = poke,
                            function = configurationSection.getString("function") ?: ""
                        )
                        card[it] = newCard
                    }
                }

            }
        }
    }
    fun reload() {
        tip.clear()
        card.clear()
        viewConfigurationMap.clear()
        YuCobblemonCard.INSTANCE.reloadConfig()
        config = YuCobblemonCard.INSTANCE.config
        message = Message(config)
        load()
    }


}