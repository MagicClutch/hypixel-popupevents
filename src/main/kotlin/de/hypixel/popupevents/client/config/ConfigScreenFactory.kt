package de.hypixel.popupevents.client.config

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen
import net.minecraft.client.gui.screens.Screen

object ConfigScreenFactory {
    fun create(parent: Screen?): Screen {
        return ResourcefulConfigScreen.make(ConfigManager.configurator, PopupConfigData::class.java)
            .withParent(parent)
            .build()
    }
}
