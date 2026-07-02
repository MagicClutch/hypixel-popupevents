package de.hypixel.popupevents.client

import de.hypixel.popupevents.client.chat.HypixelChatListener
import de.hypixel.popupevents.client.command.PopupTestCommands
import de.hypixel.popupevents.client.config.ConfigManager
import de.hypixel.popupevents.client.input.PopupKeybinds
import de.hypixel.popupevents.client.render.PopupHudRenderer
import de.hypixel.popupevents.client.screen.DeferredScreenOpener
import net.fabricmc.api.ClientModInitializer

class PopupeventsClient : ClientModInitializer {
    override fun onInitializeClient() {
        ConfigManager.load()
        DeferredScreenOpener.register()
        PopupKeybinds.register()
        PopupTestCommands.register()
        HypixelChatListener.register()
        PopupHudRenderer.register()
    }
}
