package de.hypixel.popupevents.client.config

import com.teamresourceful.resourcefulconfig.api.loader.Configurator
import de.hypixel.popupevents.client.MOD_ID

object ConfigManager {
    val configurator = Configurator(MOD_ID)

    val config: PopupConfig = PopupConfig
    private var registered = false

    fun load() {
        if (!registered) {
            configurator.register(PopupConfig::class.java)
            registered = true
        } else {
            configurator.loadConfig(PopupConfig::class.java)
        }
        clamp()
        save()
    }

    fun save() {
        configurator.saveConfig(PopupConfig::class.java)
    }

    fun update(block: (PopupConfig) -> Unit) {
        block(config)
        clamp()
        save()
    }

    private fun clamp() {
        if (config.x == 12 && config.y == 72) {
            config.x = -1
            config.y = 20
        }
        config.x = config.x.coerceAtLeast(-1)
        config.y = config.y.coerceAtLeast(0)
        config.width = config.width.coerceIn(160, 600)
        config.height = config.height.coerceIn(52, 220)
        config.scale = config.scale.coerceIn(0.5, 4.0)
        config.durationSeconds = config.durationSeconds.coerceIn(1.0, 30.0)
        config.backgroundOpacity = config.backgroundOpacity.coerceIn(0.0, 1.0)
        config.animationSpeed = config.animationSpeed.coerceIn(0.1, 4.0)
        config.fadeDurationSeconds = config.fadeDurationSeconds.coerceIn(0.05, 2.0)
        config.slideUpDurationSeconds = config.slideUpDurationSeconds.coerceIn(0.1, 2.0)
    }
}
