package de.hypixel.popupevents.client.popup

import de.hypixel.popupevents.client.config.ConfigManager
import de.hypixel.popupevents.client.model.PopupRequest
import de.hypixel.popupevents.client.util.CommandExecutor
import de.hypixel.popupevents.client.util.PopupSounds

object PopupManager {
    private var active: PopupRequest? = null
    private var closingStartedAt: Long? = null
    private var closeMode: CloseMode = CloseMode.FADE

    fun show(request: PopupRequest) {
        active = request
        closingStartedAt = null
        closeMode = CloseMode.FADE
        PopupSounds.newPopup()
    }

    fun dismiss() {
        if (active != null && closingStartedAt == null) {
            closingStartedAt = System.currentTimeMillis()
            closeMode = CloseMode.FADE
        }
    }

    fun accept() {
        if (active == null || closingStartedAt != null) return
        val command = active?.acceptCommand
        closeWithActionAnimation()
        PopupSounds.accept()
        if (command != null) {
            CommandExecutor.execute(command)
        }
    }

    fun decline() {
        if (active == null || closingStartedAt != null) return
        val command = active?.declineCommand
        closeWithActionAnimation()
        PopupSounds.decline()
        if (command != null) {
            CommandExecutor.execute(command)
        }
    }

    fun current(now: Long = System.currentTimeMillis()): RenderState? {
        val request = active ?: return null
        val config = ConfigManager.config
        val durationMillis = (config.durationSeconds * 1000.0).toLong()
        val fadeMillis = (config.fadeDurationSeconds * 1000.0 / config.animationSpeed).toLong().coerceAtLeast(1L)
        val slideMillis = (config.slideUpDurationSeconds * 1000.0 / config.animationSpeed).toLong().coerceAtLeast(1L)
        val elapsed = now - request.receivedAtMillis
        val lifetimeProgress = (elapsed.toFloat() / durationMillis.toFloat()).coerceIn(0.0f, 1.0f)
        val remainingProgress = 1.0f - lifetimeProgress

        if (elapsed >= durationMillis && closingStartedAt == null) {
            closingStartedAt = now
            closeMode = CloseMode.FADE
        }

        val closingAt = closingStartedAt
        val closeMillis = if (closeMode == CloseMode.SLIDE_UP) slideMillis else fadeMillis
        val closeProgress = if (closingAt != null) {
            ((now - closingAt).toFloat() / closeMillis.toFloat()).coerceIn(0.0f, 1.0f)
        } else 0.0f

        val alpha = when {
            !config.animations -> if (closingAt == null) 1.0f else 0.0f
            closingAt != null -> 1.0f - closeProgress
            else -> (elapsed.toFloat() / fadeMillis.toFloat()).coerceIn(0.0f, 1.0f)
        }

        val slideOffset = if (config.animations && closingAt != null && closeMode == CloseMode.SLIDE_UP) {
            val eased = 1.0f - (1.0f - closeProgress) * (1.0f - closeProgress) * (1.0f - closeProgress)
            (-48.0f * eased)
        } else 0.0f

        if (closingAt != null && now - closingAt >= closeMillis) {
            active = null
            closingStartedAt = null
            closeMode = CloseMode.FADE
            return null
        }

        return RenderState(request, alpha, slideOffset, remainingProgress)
    }

    private fun closeWithActionAnimation() {
        closingStartedAt = System.currentTimeMillis()
        closeMode = CloseMode.SLIDE_UP
    }

    private enum class CloseMode {
        FADE,
        SLIDE_UP
    }

    data class RenderState(
        val request: PopupRequest,
        val alpha: Float,
        val yOffset: Float,
        val remainingProgress: Float
    )
}
