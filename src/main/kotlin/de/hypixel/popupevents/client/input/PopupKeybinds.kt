package de.hypixel.popupevents.client.input

import com.mojang.blaze3d.platform.InputConstants
import de.hypixel.popupevents.client.MOD_ID
import de.hypixel.popupevents.client.popup.PopupManager
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier

object PopupKeybinds {
    private lateinit var accept: KeyMapping
    private lateinit var decline: KeyMapping
    private lateinit var dismiss: KeyMapping
    private val category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "keys"))
    private var acceptWasDown = false
    private var declineWasDown = false
    private var dismissWasDown = false

    fun register() {
        accept = KeyMappingHelper.registerKeyMapping(
            KeyMapping("key.popupevents.accept", InputConstants.KEY_Y, category)
        )
        decline = KeyMappingHelper.registerKeyMapping(
            KeyMapping("key.popupevents.decline", InputConstants.KEY_N, category)
        )
        dismiss = KeyMappingHelper.registerKeyMapping(
            KeyMapping("key.popupevents.dismiss", InputConstants.KEY_X, category)
        )

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            handleKeybinds()
        })
    }

    private fun handleKeybinds() {
        if (Minecraft.getInstance().screen != null) {
            acceptWasDown = accept.isDown
            declineWasDown = decline.isDown
            dismissWasDown = dismiss.isDown
            return
        }

        var accepted = false
        while (accept.consumeClick()) {
            PopupManager.accept()
            accepted = true
        }

        var declined = false
        while (decline.consumeClick()) {
            PopupManager.decline()
            declined = true
        }

        var dismissed = false
        while (dismiss.consumeClick()) {
            PopupManager.dismiss()
            dismissed = true
        }

        val acceptDown = accept.isDown
        if (!accepted && acceptDown && !acceptWasDown) {
            PopupManager.accept()
        }
        acceptWasDown = acceptDown

        val declineDown = decline.isDown
        if (!declined && declineDown && !declineWasDown) {
            PopupManager.decline()
        }
        declineWasDown = declineDown

        val dismissDown = dismiss.isDown
        if (!dismissed && dismissDown && !dismissWasDown) {
            PopupManager.dismiss()
        }
        dismissWasDown = dismissDown
    }
}
