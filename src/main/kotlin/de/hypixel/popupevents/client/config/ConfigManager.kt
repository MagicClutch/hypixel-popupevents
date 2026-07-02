package de.hypixel.popupevents.client.config

import com.google.gson.GsonBuilder
import de.hypixel.popupevents.client.MOD_ID
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

object ConfigManager {
    private val logger = LoggerFactory.getLogger(MOD_ID)
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val path: Path = FabricLoader.getInstance().configDir.resolve("$MOD_ID.json")

    var config: PopupConfig = PopupConfig()
        private set

    fun load() {
        config = runCatching {
            if (!path.exists()) {
                save()
                return
            }
            gson.fromJson(path.readText(), PopupConfig::class.java) ?: PopupConfig()
        }.getOrElse {
            logger.warn("Failed to load $MOD_ID config; using defaults.", it)
            PopupConfig()
        }
        clamp()
    }

    fun save() {
        runCatching {
            Files.createDirectories(path.parent)
            path.writeText(gson.toJson(config))
        }.onFailure {
            logger.warn("Failed to save $MOD_ID config.", it)
        }
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
        if (config.textAlignment == null) {
            config.textAlignment = PopupTextAlignment.CENTER
        }
        config.animationSpeed = config.animationSpeed.coerceIn(0.1, 4.0)
        config.fadeDurationSeconds = config.fadeDurationSeconds.coerceIn(0.05, 2.0)
        config.slideUpDurationSeconds = config.slideUpDurationSeconds.coerceIn(0.1, 2.0)
    }
}
