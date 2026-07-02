package de.hypixel.popupevents.client.config

import de.hypixel.popupevents.client.screen.DeferredScreenOpener
import dev.isxander.yacl3.api.ButtonOption
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

object ConfigScreenFactory {
    fun create(parent: Screen?): Screen {
        return YetAnotherConfigLib.createBuilder()
            .title(Component.literal("Hypixel Request Popups"))
            .category(general())
            .category(popup())
            .category(animation())
            .category(debug())
            .save { ConfigManager.save() }
            .build()
            .generateScreen(parent)
    }

    private fun general(): ConfigCategory {
        return ConfigCategory.createBuilder()
            .name(Component.literal("General"))
            .option(booleanOption("Enable mod", ConfigManager.config.enabled) { ConfigManager.config.enabled = it })
            .option(booleanOption("Enable trade popups", ConfigManager.config.tradePopups) { ConfigManager.config.tradePopups = it })
            .option(booleanOption("Enable party popups", ConfigManager.config.partyPopups) { ConfigManager.config.partyPopups = it })
            .option(booleanOption("Enable friend popups", ConfigManager.config.friendPopups) { ConfigManager.config.friendPopups = it })
            .option(booleanOption("Enable guild popups", ConfigManager.config.guildPopups) { ConfigManager.config.guildPopups = it })
            .option(booleanOption("Enable duel popups", ConfigManager.config.duelPopups) { ConfigManager.config.duelPopups = it })
            .build()
    }

    private fun popup(): ConfigCategory {
        return ConfigCategory.createBuilder()
            .name(Component.literal("Popup"))
            .option(
                ButtonOption.createBuilder()
                    .name(Component.literal("Open Editor"))
                    .description(OptionDescription.of(Component.literal("Drag the popup and use the scroll wheel to resize it.")))
                    .action { _, _ ->
                        DeferredScreenOpener.open { PopupPositionEditScreen() }
                    }
                    .build()
            )
            .option(doubleOption("Popup duration", ConfigManager.config.durationSeconds, 1.0, 30.0, 0.25) { ConfigManager.config.durationSeconds = it })
            .option(doubleOption("Background opacity", ConfigManager.config.backgroundOpacity, 0.0, 1.0, 0.05) { ConfigManager.config.backgroundOpacity = it })
            .option(booleanOption("Background enabled", ConfigManager.config.backgroundEnabled) { ConfigManager.config.backgroundEnabled = it })
            .option(booleanOption("Rounded corners", ConfigManager.config.roundedCorners) { ConfigManager.config.roundedCorners = it })
            .option(textAlignmentOption())
            .build()
    }

    private fun animation(): ConfigCategory {
        return ConfigCategory.createBuilder()
            .name(Component.literal("Animation"))
            .option(booleanOption("Enable animations", ConfigManager.config.animations) { ConfigManager.config.animations = it })
            .option(doubleOption("Animation speed", ConfigManager.config.animationSpeed, 0.1, 4.0, 0.1) { ConfigManager.config.animationSpeed = it })
            .option(doubleOption("Fade duration", ConfigManager.config.fadeDurationSeconds, 0.05, 2.0, 0.05) { ConfigManager.config.fadeDurationSeconds = it })
            .option(doubleOption("Slide up duration", ConfigManager.config.slideUpDurationSeconds, 0.1, 2.0, 0.05) { ConfigManager.config.slideUpDurationSeconds = it })
            .build()
    }

    private fun debug(): ConfigCategory {
        return ConfigCategory.createBuilder()
            .name(Component.literal("Debug"))
            .option(booleanOption("Enable debug logging", ConfigManager.config.debugLogging) { ConfigManager.config.debugLogging = it })
            .option(booleanOption("Print detected JSON chat component", ConfigManager.config.printJson) { ConfigManager.config.printJson = it })
            .option(booleanOption("Print extracted commands", ConfigManager.config.printCommands) { ConfigManager.config.printCommands = it })
            .build()
    }

    private fun booleanOption(name: String, defaultValue: Boolean, setter: (Boolean) -> Unit): Option<Boolean> {
        return Option.createBuilder<Boolean>()
            .name(Component.literal(name))
            .description(OptionDescription.EMPTY)
            .binding(defaultValue, { currentBooleanValue(name) }, setter)
            .controller(BooleanControllerBuilder::create)
            .build()
    }

    private fun intOption(name: String, defaultValue: Int, min: Int, max: Int, setter: (Int) -> Unit): Option<Int> {
        return Option.createBuilder<Int>()
            .name(Component.literal(name))
            .description(OptionDescription.EMPTY)
            .binding(defaultValue, { currentIntValue(name) }, setter)
            .controller { option -> IntegerSliderControllerBuilder.create(option).range(min, max).step(1) }
            .build()
    }

    private fun doubleOption(name: String, defaultValue: Double, min: Double, max: Double, step: Double, setter: (Double) -> Unit): Option<Double> {
        return Option.createBuilder<Double>()
            .name(Component.literal(name))
            .description(OptionDescription.EMPTY)
            .binding(defaultValue, { currentDoubleValue(name) }, setter)
            .controller { option -> DoubleSliderControllerBuilder.create(option).range(min, max).step(step) }
            .build()
    }

    private fun textAlignmentOption(): Option<PopupTextAlignment> {
        return Option.createBuilder<PopupTextAlignment>()
            .name(Component.literal("Text alignment"))
            .description(OptionDescription.of(Component.literal("Controls how the popup title, question, and key hints are aligned.")))
            .binding(
                PopupTextAlignment.CENTER,
                { ConfigManager.config.textAlignment ?: PopupTextAlignment.CENTER },
                { ConfigManager.config.textAlignment = it }
            )
            .controller { option ->
                EnumControllerBuilder.create(option)
                    .enumClass(PopupTextAlignment::class.java)
                    .formatValue { Component.literal(it.displayName()) }
            }
            .build()
    }

    private fun currentBooleanValue(name: String): Boolean = when (name) {
        "Enable mod" -> ConfigManager.config.enabled
        "Enable trade popups" -> ConfigManager.config.tradePopups
        "Enable party popups" -> ConfigManager.config.partyPopups
        "Enable friend popups" -> ConfigManager.config.friendPopups
        "Enable guild popups" -> ConfigManager.config.guildPopups
        "Enable duel popups" -> ConfigManager.config.duelPopups
        "Background enabled" -> ConfigManager.config.backgroundEnabled
        "Rounded corners" -> ConfigManager.config.roundedCorners
        "Enable animations" -> ConfigManager.config.animations
        "Enable debug logging" -> ConfigManager.config.debugLogging
        "Print detected JSON chat component" -> ConfigManager.config.printJson
        "Print extracted commands" -> ConfigManager.config.printCommands
        else -> false
    }

    private fun currentIntValue(name: String): Int = when (name) {
        "X Position" -> ConfigManager.config.x
        "Y Position" -> ConfigManager.config.y
        "Width" -> ConfigManager.config.width
        "Height" -> ConfigManager.config.height
        else -> 0
    }

    private fun currentDoubleValue(name: String): Double = when (name) {
        "Popup duration" -> ConfigManager.config.durationSeconds
        "Background opacity" -> ConfigManager.config.backgroundOpacity
        "Animation speed" -> ConfigManager.config.animationSpeed
        "Fade duration" -> ConfigManager.config.fadeDurationSeconds
        "Slide up duration" -> ConfigManager.config.slideUpDurationSeconds
        "Scale" -> ConfigManager.config.scale
        else -> 0.0
    }
}
