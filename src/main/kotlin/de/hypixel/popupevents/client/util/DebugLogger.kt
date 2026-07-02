package de.hypixel.popupevents.client.util

import de.hypixel.popupevents.client.MOD_ID
import de.hypixel.popupevents.client.config.ConfigManager
import org.slf4j.LoggerFactory

object DebugLogger {
    private val logger = LoggerFactory.getLogger(MOD_ID)

    fun debug(message: String) {
        if (ConfigManager.config.debugLogging) {
            logger.info("[debug] $message")
        }
    }
}
