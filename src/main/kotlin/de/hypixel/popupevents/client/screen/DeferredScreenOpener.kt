package de.hypixel.popupevents.client.screen

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.gui.screens.Screen

object DeferredScreenOpener {
    private var pendingFactory: (() -> Screen)? = null
    private var ticksUntilOpen = 0

    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client ->
            val factory = pendingFactory ?: return@EndTick
            if (ticksUntilOpen > 0) {
                ticksUntilOpen--
                return@EndTick
            }

            pendingFactory = null
            client.setScreen(factory())
        })
    }

    fun open(factory: () -> Screen) {
        pendingFactory = factory
        ticksUntilOpen = 2
    }
}
