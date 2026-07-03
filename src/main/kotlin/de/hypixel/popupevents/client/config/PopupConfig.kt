package de.hypixel.popupevents.client.config

object PopupConfig {
    var enabled: Boolean
        get() = PopupConfigData.enabled
        set(value) {
            PopupConfigData.enabled = value
        }

    var tradePopups: Boolean
        get() = PopupConfigData.tradePopups
        set(value) {
            PopupConfigData.tradePopups = value
        }

    var partyPopups: Boolean
        get() = PopupConfigData.partyPopups
        set(value) {
            PopupConfigData.partyPopups = value
        }

    var friendPopups: Boolean
        get() = PopupConfigData.friendPopups
        set(value) {
            PopupConfigData.friendPopups = value
        }

    var guildPopups: Boolean
        get() = PopupConfigData.guildPopups
        set(value) {
            PopupConfigData.guildPopups = value
        }

    var duelPopups: Boolean
        get() = PopupConfigData.duelPopups
        set(value) {
            PopupConfigData.duelPopups = value
        }

    var x: Int
        get() = PopupConfigData.x
        set(value) {
            PopupConfigData.x = value
        }

    var y: Int
        get() = PopupConfigData.y
        set(value) {
            PopupConfigData.y = value
        }

    var width: Int
        get() = PopupConfigData.width
        set(value) {
            PopupConfigData.width = value
        }

    var height: Int
        get() = PopupConfigData.height
        set(value) {
            PopupConfigData.height = value
        }

    var scale: Double
        get() = PopupConfigData.scale
        set(value) {
            PopupConfigData.scale = value
        }

    var durationSeconds: Double
        get() = PopupConfigData.durationSeconds
        set(value) {
            PopupConfigData.durationSeconds = value
        }

    var backgroundOpacity: Double
        get() = PopupConfigData.backgroundOpacity
        set(value) {
            PopupConfigData.backgroundOpacity = value
        }

    var backgroundEnabled: Boolean
        get() = PopupConfigData.backgroundEnabled
        set(value) {
            PopupConfigData.backgroundEnabled = value
        }

    var textAlignment: PopupTextAlignment
        get() = PopupConfigData.textAlignment
        set(value) {
            PopupConfigData.textAlignment = value
        }

    var animations: Boolean
        get() = PopupConfigData.animations
        set(value) {
            PopupConfigData.animations = value
        }

    var animationSpeed: Double
        get() = PopupConfigData.animationSpeed
        set(value) {
            PopupConfigData.animationSpeed = value
        }

    var fadeDurationSeconds: Double
        get() = PopupConfigData.fadeDurationSeconds
        set(value) {
            PopupConfigData.fadeDurationSeconds = value
        }

    var slideUpDurationSeconds: Double
        get() = PopupConfigData.slideUpDurationSeconds
        set(value) {
            PopupConfigData.slideUpDurationSeconds = value
        }

    var debugLogging: Boolean
        get() = PopupConfigData.debugLogging
        set(value) {
            PopupConfigData.debugLogging = value
        }

    var printJson: Boolean
        get() = PopupConfigData.printJson
        set(value) {
            PopupConfigData.printJson = value
        }

    var printCommands: Boolean
        get() = PopupConfigData.printCommands
        set(value) {
            PopupConfigData.printCommands = value
        }
}
