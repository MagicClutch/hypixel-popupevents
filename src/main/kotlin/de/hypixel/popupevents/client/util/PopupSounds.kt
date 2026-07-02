package de.hypixel.popupevents.client.util

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.sounds.SoundEvents

object PopupSounds {
    fun newPopup() {
        play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, 0.75f))
    }

    fun accept() {
        play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.25f))
    }

    fun decline() {
        play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0f))
    }

    private fun play(sound: SimpleSoundInstance) {
        Minecraft.getInstance().soundManager.play(sound)
    }
}
