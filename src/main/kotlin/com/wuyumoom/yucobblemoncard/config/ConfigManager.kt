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
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration

object ConfigManager {
    var config = YuCobblemonCard.INSTANCE.config
    var tip: MutableList<String> = mutableListOf()
    val card = mutableMapOf<String, Card>()
    val viewConfigurationMap: MutableMap<String, ViewConfiguration> = HashMap()
    var message: Message = Message(config)

    lateinit var movesGUIDefaultID : Material
    lateinit var NatureGUIDefaultID : Material

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
            if (replace == "NatureGUI"){
                val string = loadConfiguration.getString("defaultID")?: "STICK"
                var material = Material.getMaterial( string)
                NatureGUIDefaultID = material ?: Material.STICK
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                            "体型卡"->{
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                            "球种卡"->{
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                            "性格卡"->{
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                            "性别卡" -> {
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                        val configurationSection = configurationSection.getConfigurationSection("use.poke")
                        if (configurationSection == null){
                            return@forEach
                        }
                        val poke: MutableList<Species> = loadPoke(configurationSection)
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
                        val configurationSection = configurationSection.getConfigurationSection("use.poke")
                        if (configurationSection == null){
                            return@forEach
                        }
                        val poke: MutableList<Species> = loadPoke(configurationSection)
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
                        val configurationSection = configurationSection.getConfigurationSection("use.poke")
                        if (configurationSection == null){
                            return@forEach
                        }
                        val poke: MutableList<Species> = loadPoke(configurationSection)
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
                                val configurationSection = configurationSection.getConfigurationSection("use.poke")
                                if (configurationSection == null){
                                    return@forEach
                                }
                                val poke: MutableList<Species> = loadPoke(configurationSection)
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
                            }
                            else -> {
                            val configurationSection = configurationSection.getConfigurationSection("use.poke")
                            if (configurationSection == null){
                                return@forEach
                            }
                            val poke: MutableList<Species> = loadPoke(configurationSection)
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


    private fun loadPoke(configurationSection: ConfigurationSection): MutableList<Species>{
        val poke: MutableList<Species> = mutableListOf()
        val loadSpecies = YuSpecies.loadSpecies(configurationSection)
        val banPoke = configurationSection.getConfigurationSection("banPoke")
        if (banPoke != null){
            loadSpecies.removeAll(YuSpecies.loadSpecies(banPoke)   )
        }
        poke.addAll(loadSpecies)
        return poke
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