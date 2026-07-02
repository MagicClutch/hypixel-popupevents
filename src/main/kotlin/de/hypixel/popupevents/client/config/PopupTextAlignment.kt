package de.hypixel.popupevents.client.config

enum class PopupTextAlignment(private val displayName: String) {
    LEFT("Left"),
    CENTER("Center"),
    RIGHT("Right");

    fun displayName(): String = displayName
}
