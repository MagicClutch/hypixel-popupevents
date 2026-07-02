package de.hypixel.popupevents.client.render

import de.hypixel.popupevents.client.MOD_ID
import de.hypixel.popupevents.client.config.ConfigManager
import de.hypixel.popupevents.client.config.PopupTextAlignment
import de.hypixel.popupevents.client.input.PopupKeybinds
import de.hypixel.popupevents.client.popup.PopupManager
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.Identifier
import kotlin.math.roundToInt

object PopupHudRenderer {
    fun register() {
        HudElementRegistry.attachElementAfter(
            VanillaHudElements.CHAT,
            Identifier.fromNamespaceAndPath(MOD_ID, "request_popup"),
            HudElement { graphics, _ -> render(graphics) }
        )
    }

    private fun render(graphics: GuiGraphics) {
        val state = PopupManager.current() ?: return
        renderRequest(graphics, state.request, state.alpha, state.yOffset, state.remainingProgress)
    }

    fun renderPreview(graphics: GuiGraphics) {
        renderRequest(
            graphics,
            de.hypixel.popupevents.client.model.PopupRequest(
                de.hypixel.popupevents.client.model.RequestType.TRADE,
                "MagicClutch",
                "/tradeaccept preview",
                null
            ),
            1.0f,
            0.0f,
            1.0f
        )
    }

    fun bounds(screenWidth: Int): Bounds {
        val config = ConfigManager.config
        val scale = config.scale.toFloat()
        val width = (config.width * scale).roundToInt()
        val height = (config.height * scale).roundToInt()
        val x = if (config.x < 0) ((screenWidth - width) / 2).coerceAtLeast(0) else config.x
        return Bounds(x, config.y, width, height)
    }

    private fun renderRequest(
        graphics: GuiGraphics,
        request: de.hypixel.popupevents.client.model.PopupRequest,
        alpha: Float,
        yOffset: Float,
        remainingProgress: Float
    ) {
        if (alpha <= 0.0f) return

        val config = ConfigManager.config
        val textRenderer = Minecraft.getInstance().font
        val bounds = bounds(Minecraft.getInstance().window.guiScaledWidth)
        val x = bounds.x
        val y = bounds.y + yOffset.roundToInt()
        val width = bounds.width
        val height = bounds.height
        val scale = config.scale.toFloat()
        val padding = 10
        val opacity = (config.backgroundOpacity * alpha * 255.0).roundToInt().coerceIn(0, 255)
        val borderAlpha = (alpha * 180.0f).roundToInt().coerceIn(0, 255)
        val textAlpha = (alpha * 255.0f).roundToInt().coerceIn(0, 255)

        if (config.backgroundEnabled) {
            graphics.fill(x, y, x + width, y + height, opacity shl 24)
        }
        graphics.fill(x, y, x + width, y + 1, (borderAlpha shl 24) or 0xAAAAAA)
        graphics.fill(x, y + height - 1, x + width, y + height, (borderAlpha shl 24) or 0x555555)
        graphics.fill(x, y, x + 1, y + height, (borderAlpha shl 24) or 0x888888)
        graphics.fill(x + width - 1, y, x + width, y + height, (borderAlpha shl 24) or 0x888888)

        val title = request.title
        val question = request.question
        val color = (textAlpha shl 24) or 0xFFFFFF
        val acceptColor = (textAlpha shl 24) or 0x55FF55
        val declineColor = (textAlpha shl 24) or 0xFF5555
        val dismissColor = (textAlpha shl 24) or 0x55DDFF
        val progressBack = ((alpha * 95.0f).roundToInt().coerceIn(0, 255) shl 24) or 0x1F1F1F
        val progressFill = (textAlpha shl 24) or 0x55DDFF
        val alignment = config.textAlignment ?: PopupTextAlignment.CENTER
        val controls = listOf(
            TextSegment("[${PopupKeybinds.acceptKeyName()}] Accept", acceptColor),
            TextSegment("      ", color),
            TextSegment("[${PopupKeybinds.declineKeyName()}] Decline", declineColor)
        )
        val dismissControl = listOf(TextSegment("[${PopupKeybinds.dismissKeyName()}] Dismiss", dismissColor))

        val pose = graphics.pose()
        pose.pushMatrix()
        pose.translate(x.toFloat(), y.toFloat())
        pose.scale(scale, scale)
        graphics.drawString(textRenderer, title, alignedTextX(title, padding, config.width, alignment), 10, color, true)
        graphics.drawString(textRenderer, question, alignedTextX(question, padding, config.width, alignment), 28, color, true)
        renderSegments(graphics, controls, padding, config.width, alignment, config.height - 29)
        renderSegments(graphics, dismissControl, padding, config.width, alignment, config.height - 18)
        renderProgressBar(graphics, padding, config.width, config.height, remainingProgress, progressBack, progressFill)
        pose.popMatrix()
    }

    private fun renderSegments(
        graphics: GuiGraphics,
        segments: List<TextSegment>,
        padding: Int,
        width: Int,
        alignment: PopupTextAlignment,
        y: Int
    ) {
        val textRenderer = Minecraft.getInstance().font
        var x = alignedTextX(segments.joinToString("") { it.text }, padding, width, alignment)
        for (segment in segments) {
            graphics.drawString(textRenderer, segment.text, x, y, segment.color, true)
            x += textRenderer.width(segment.text)
        }
    }

    private fun renderProgressBar(
        graphics: GuiGraphics,
        padding: Int,
        width: Int,
        height: Int,
        remainingProgress: Float,
        backgroundColor: Int,
        fillColor: Int
    ) {
        val barX = padding
        val barY = (height - 8).coerceAtLeast(0)
        val barWidth = (width - padding * 2).coerceAtLeast(1)
        val barHeight = 3
        val fillWidth = (barWidth * remainingProgress.coerceIn(0.0f, 1.0f)).roundToInt().coerceIn(0, barWidth)

        graphics.fill(barX, barY, barX + barWidth, barY + barHeight, backgroundColor)
        if (fillWidth > 0) {
            graphics.fill(barX, barY, barX + fillWidth, barY + barHeight, fillColor)
        }
    }

    private fun alignedTextX(text: String, padding: Int, width: Int, alignment: PopupTextAlignment): Int {
        val textWidth = Minecraft.getInstance().font.width(text)
        val minX = padding
        val maxX = (width - padding - textWidth).coerceAtLeast(minX)
        return when (alignment) {
            PopupTextAlignment.LEFT -> minX
            PopupTextAlignment.CENTER -> ((width - textWidth) / 2).coerceIn(minX, maxX)
            PopupTextAlignment.RIGHT -> maxX
        }
    }

    data class Bounds(val x: Int, val y: Int, val width: Int, val height: Int)

    private data class TextSegment(val text: String, val color: Int)
}
