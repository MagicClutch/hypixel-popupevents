package de.hypixel.popupevents.client.config

import de.hypixel.popupevents.client.render.PopupHudRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component

class PopupPositionEditScreen : Screen(Component.literal("Popup Editor")) {
    private var dragging = false
    private var dragOffsetX = 0.0
    private var dragOffsetY = 0.0

    override fun init() {
        val buttonWidth = 72
        val buttonHeight = 20
        val spacing = 5
        val startX = width - buttonWidth - 10

        addRenderableWidget(
            Button.builder(Component.literal("Center X")) {
                ConfigManager.update { config -> config.x = -1 }
            }.pos(startX, 10).size(buttonWidth, buttonHeight).build()
        )

        addRenderableWidget(
            Button.builder(Component.literal("Top")) {
                ConfigManager.update { config -> config.y = 20 }
            }.pos(startX, 10 + buttonHeight + spacing).size(buttonWidth, buttonHeight).build()
        )

        addRenderableWidget(
            Button.builder(Component.literal("Reset")) {
                ConfigManager.update { config ->
                    config.x = -1
                    config.y = 20
                    config.width = 260
                    config.height = 76
                    config.scale = 1.0
                }
            }.pos(startX, 10 + (buttonHeight + spacing) * 2).size(buttonWidth, buttonHeight).build()
        )
    }

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        super.extractRenderState(graphics, mouseX, mouseY, delta)
        PopupHudRenderer.renderPreview(graphics)

        val font = Minecraft.getInstance().font
        graphics.text(font, "Drag popup to move", 10, height - 34, 0xAAAAAA, true)
        graphics.text(font, "Scroll wheel over popup: change size", 10, height - 20, 0xAAAAAA, true)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        if (isInsidePopup(mouseX, mouseY)) {
            ConfigManager.update { config ->
                config.scale = (config.scale + verticalAmount * 0.1).coerceIn(0.5, 4.0)
            }
            return true
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun mouseClicked(event: MouseButtonEvent, isDoubleClick: Boolean): Boolean {
        if (event.button() == 0 && isInsidePopup(event.x(), event.y())) {
            val bounds = PopupHudRenderer.bounds(width)
            dragging = true
            dragOffsetX = event.x() - bounds.x
            dragOffsetY = event.y() - bounds.y
            return true
        }

        return super.mouseClicked(event, isDoubleClick)
    }

    override fun mouseDragged(event: MouseButtonEvent, deltaX: Double, deltaY: Double): Boolean {
        if (dragging) {
            val bounds = PopupHudRenderer.bounds(width)
            ConfigManager.update { config ->
                config.x = (event.x() - dragOffsetX).toInt().coerceIn(0, (width - bounds.width).coerceAtLeast(0))
                config.y = (event.y() - dragOffsetY).toInt().coerceIn(0, (height - bounds.height).coerceAtLeast(0))
            }
            return true
        }

        return super.mouseDragged(event, deltaX, deltaY)
    }

    override fun mouseReleased(event: MouseButtonEvent): Boolean {
        dragging = false
        ConfigManager.save()
        return super.mouseReleased(event)
    }

    override fun onClose() {
        dragging = false
        ConfigManager.save()
        super.onClose()
    }

    override fun isPauseScreen(): Boolean = false

    private fun isInsidePopup(mouseX: Double, mouseY: Double): Boolean {
        val bounds = PopupHudRenderer.bounds(width)
        return mouseX >= bounds.x &&
            mouseX <= bounds.x + bounds.width &&
            mouseY >= bounds.y &&
            mouseY <= bounds.y + bounds.height
    }
}
