package de.hypixel.popupevents.client.config

import com.teamresourceful.resourcefulconfig.api.annotations.Comment
import com.teamresourceful.resourcefulconfig.api.annotations.Config
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigButton
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption
import de.hypixel.popupevents.client.MOD_ID
import de.hypixel.popupevents.client.MOD_NAME
import de.hypixel.popupevents.client.screen.DeferredScreenOpener

@ConfigInfo(
    title = MOD_NAME,
    description = "Client-side Hypixel party, trade, friend, guild, and duel request HUD popups."
)
@Config(value = MOD_ID)
object PopupConfig {
    @JvmField
    @ConfigEntry(id = "enabled", translation = "Enable mod")
    var enabled: Boolean = true

    @JvmField
    @ConfigEntry(id = "tradePopups", translation = "Enable trade popups")
    var tradePopups: Boolean = true

    @JvmField
    @ConfigEntry(id = "partyPopups", translation = "Enable party popups")
    var partyPopups: Boolean = true

    @JvmField
    @ConfigEntry(id = "friendPopups", translation = "Enable friend popups")
    var friendPopups: Boolean = true

    @JvmField
    @ConfigEntry(id = "guildPopups", translation = "Enable guild popups")
    var guildPopups: Boolean = true

    @JvmField
    @ConfigEntry(id = "duelPopups", translation = "Enable duel popups")
    var duelPopups: Boolean = true

    @JvmField
    @ConfigButton(title = "Popup editor", text = "Open Editor")
    @Comment("Drag the popup and use the scroll wheel to resize it.")
    val openEditor: Runnable = Runnable {
        DeferredScreenOpener.open { PopupPositionEditScreen() }
    }

    @JvmField
    @ConfigOption.Hidden
    @ConfigEntry(id = "x")
    var x: Int = -1

    @JvmField
    @ConfigOption.Hidden
    @ConfigEntry(id = "y")
    var y: Int = 20

    @JvmField
    @ConfigOption.Hidden
    @ConfigEntry(id = "width")
    var width: Int = 260

    @JvmField
    @ConfigOption.Hidden
    @ConfigEntry(id = "height")
    var height: Int = 76

    @JvmField
    @ConfigOption.Hidden
    @ConfigEntry(id = "scale")
    var scale: Double = 1.0

    @JvmField
    @ConfigOption.Separator(value = "Popup", description = "Display and layout options.")
    @ConfigOption.Slider
    @ConfigOption.Range(min = 1.0, max = 30.0)
    @ConfigEntry(id = "durationSeconds", translation = "Popup duration")
    var durationSeconds: Double = 5.0

    @JvmField
    @ConfigOption.Slider
    @ConfigOption.Range(min = 0.0, max = 1.0)
    @ConfigEntry(id = "backgroundOpacity", translation = "Background opacity")
    var backgroundOpacity: Double = 0.72

    @JvmField
    @ConfigEntry(id = "backgroundEnabled", translation = "Background enabled")
    var backgroundEnabled: Boolean = true

    @JvmField
    @ConfigEntry(id = "textAlignment", translation = "Text alignment")
    @Comment("Controls how the popup title, question, and key hints are aligned.")
    var textAlignment: PopupTextAlignment = PopupTextAlignment.CENTER

    @JvmField
    @ConfigOption.Separator(value = "Animation", description = "Popup transition timing.")
    @ConfigEntry(id = "animations", translation = "Enable animations")
    var animations: Boolean = true

    @JvmField
    @ConfigOption.Slider
    @ConfigOption.Range(min = 0.1, max = 4.0)
    @ConfigEntry(id = "animationSpeed", translation = "Animation speed")
    var animationSpeed: Double = 1.0

    @JvmField
    @ConfigOption.Slider
    @ConfigOption.Range(min = 0.05, max = 2.0)
    @ConfigEntry(id = "fadeDurationSeconds", translation = "Fade duration")
    var fadeDurationSeconds: Double = 0.22

    @JvmField
    @ConfigOption.Slider
    @ConfigOption.Range(min = 0.1, max = 2.0)
    @ConfigEntry(id = "slideUpDurationSeconds", translation = "Slide up duration")
    var slideUpDurationSeconds: Double = 0.5

    @JvmField
    @ConfigOption.Separator(value = "Debug", description = "Development logging options.")
    @ConfigEntry(id = "debugLogging", translation = "Enable debug logging")
    var debugLogging: Boolean = false

    @JvmField
    @ConfigEntry(id = "printJson", translation = "Print detected JSON chat component")
    var printJson: Boolean = false

    @JvmField
    @ConfigEntry(id = "printCommands", translation = "Print extracted commands")
    var printCommands: Boolean = false
}
