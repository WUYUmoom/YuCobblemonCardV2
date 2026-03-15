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
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration

object ConfigManager {
    var config = YuCobblemonCard.INSTANCE.config
    var tip: MutableList<String> = mutableListOf()
    val card = mutableMapOf<String, Card>()
    val viewConfigurationMap: MutableMap<String, ViewConfiguration> = HashMap()
    var message: Message = Message(config)

    lateinit var movesGUIDefaultID : Material

    fun load() {
        config.getStringList("tip").map {
            tip.add(BukkitAPI.onReplace(it))
        }
        FileAPI.folderFiles(YuCobblemonCard.INSTANCE, "view", YuCobblemonCard.pluginFile).forEach {
            val replace = it.name.replace(".yml", "")
            val loadConfiguration = YamlConfiguration.loadConfiguration(it)
            val viewConfiguration = ViewConfiguration(loadConfiguration)
            viewConfigurationMap[replace] = viewConfiguration
            if (replace == "MovesGUI"){
                val string = loadConfiguration.getString("defaultID")?: "STICK"
                var material = Material.getMaterial( string)
                movesGUIDefaultID = material ?: Material.STICK
            }
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
                    STATS -> {
                        when (it) {
                            "超训卡" -> {
                                val poke: MutableList<Species> = mutableListOf()
                                configurationSection.getStringList("use.poke").map {
                                    poke.add(YuSpecies.getSpecies(it))
                                }
                                val newCard = HyperTrainCard(
                                    type = cardType,
                                    name = it,
                                    item = ItemStackAPI.createItem(configurationSection, true),
                                    filter = UseState.valueOf((configurationSection.getString("use.filter") ?: "").uppercase()),
                                    poke = poke,
                                    addState = AddState.valueOf((configurationSection.getString("add.type") ?: "").uppercase()),
                                    value = configurationSection.getInt("add.value")
                                )
                                card[it] = newCard
                            }else -> {
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
                                pokeState = PokeState.valueOf((configurationSection.getString("add.state") ?: "").uppercase()),
                                isAdd= configurationSection.getBoolean("add.isAdd"),
                                addState = AddState.valueOf((configurationSection.getString("add.type") ?: "").uppercase()),
                                value = configurationSection.getInt("add.value")
                            )
                            card[it] = newCard
                            }
                        }

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