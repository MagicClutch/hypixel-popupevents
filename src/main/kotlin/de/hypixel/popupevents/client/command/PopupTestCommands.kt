package de.hypixel.popupevents.client.command

import com.mojang.brigadier.Command
import de.hypixel.popupevents.client.config.ConfigScreenFactory
import de.hypixel.popupevents.client.model.PopupRequest
import de.hypixel.popupevents.client.model.RequestType
import de.hypixel.popupevents.client.popup.PopupManager
import de.hypixel.popupevents.client.screen.DeferredScreenOpener
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal
import net.minecraft.client.Minecraft

object PopupTestCommands {
    private const val TEST_PLAYER = "MagicClutch"

    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal("hypixelrequestpopups")
                    .executes {
                        DeferredScreenOpener.open {
                            ConfigScreenFactory.create(Minecraft.getInstance().gui.screen())
                        }
                        Command.SINGLE_SUCCESS
                    }
            )

            dispatcher.register(
                literal("hrptest")
                    .then(literal("trade").executes { showTestPopup(RequestType.TRADE) })
                    .then(literal("party").executes { showTestPopup(RequestType.PARTY) })
                    .then(literal("friend").executes { showTestPopup(RequestType.FRIEND) })
                    .then(literal("guild").executes { showTestPopup(RequestType.GUILD) })
                    .then(literal("duel").executes { showTestPopup(RequestType.DUEL) })
            )
        }
    }

    private fun showTestPopup(type: RequestType): Int {
        PopupManager.show(
            PopupRequest(
                type = type,
                playerName = TEST_PLAYER,
                acceptCommand = acceptCommand(type),
                declineCommand = declineCommand(type)
            )
        )
        return Command.SINGLE_SUCCESS
    }

    private fun acceptCommand(type: RequestType): String = when (type) {
        RequestType.TRADE -> "/tradeaccept $TEST_PLAYER"
        RequestType.PARTY -> "/party accept $TEST_PLAYER"
        RequestType.FRIEND -> "/friend accept $TEST_PLAYER"
        RequestType.GUILD -> "/guild accept $TEST_PLAYER"
        RequestType.DUEL -> "/duel accept $TEST_PLAYER"
    }

    private fun declineCommand(type: RequestType): String? = when (type) {
        RequestType.TRADE -> null
        RequestType.PARTY -> "/party deny $TEST_PLAYER"
        RequestType.FRIEND -> "/friend deny $TEST_PLAYER"
        RequestType.GUILD -> "/guild deny $TEST_PLAYER"
        RequestType.DUEL -> "/duel deny $TEST_PLAYER"
    }
}
