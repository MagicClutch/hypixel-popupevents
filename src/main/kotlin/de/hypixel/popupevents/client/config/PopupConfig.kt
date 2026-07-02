package de.hypixel.popupevents.client.config

data class PopupConfig(
    var enabled: Boolean = true,
    var tradePopups: Boolean = true,
    var partyPopups: Boolean = true,
    var friendPopups: Boolean = true,
    var guildPopups: Boolean = true,
    var duelPopups: Boolean = true,
    var x: Int = -1,
    var y: Int = 20,
    var width: Int = 260,
    var height: Int = 76,
    var scale: Double = 1.0,
    var durationSeconds: Double = 5.0,
    var backgroundOpacity: Double = 0.72,
    var backgroundEnabled: Boolean = true,
    var roundedCorners: Boolean = true,
    var textAlignment: PopupTextAlignment? = PopupTextAlignment.CENTER,
    var animations: Boolean = true,
    var animationSpeed: Double = 1.0,
    var fadeDurationSeconds: Double = 0.22,
    var slideUpDurationSeconds: Double = 0.5,
    var debugLogging: Boolean = false,
    var printJson: Boolean = false,
    var printCommands: Boolean = false
)
