package de.hypixel.popupevents.client.util

import net.minecraft.client.Minecraft

object CommandExecutor {
    fun execute(command: String) {
        val client = Minecraft.getInstance()
        val connection = client.player?.connection ?: return
        val normalized = command.trim().removePrefix("/")
        if (normalized.isNotEmpty()) {
            connection.sendCommand(normalized)
        }
    }
}
