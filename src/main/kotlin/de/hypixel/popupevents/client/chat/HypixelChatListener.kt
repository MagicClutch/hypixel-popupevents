package de.hypixel.popupevents.client.chat

import de.hypixel.popupevents.client.config.ConfigManager
import de.hypixel.popupevents.client.popup.PopupManager
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents

object HypixelChatListener {
    fun register() {
        ClientReceiveMessageEvents.CHAT.register(ClientReceiveMessageEvents.Chat { message, _, _, _, _ ->
            handle(message)
        })

        ClientReceiveMessageEvents.GAME.register(ClientReceiveMessageEvents.Game { message, overlay ->
            if (!overlay) {
                handle(message)
            }
        })
    }

    private fun handle(message: net.minecraft.network.chat.Component) {
        if (!ConfigManager.config.enabled) {
            return
        }
        ChatComponentParser.parse(message)?.let(PopupManager::show)
    }
}
