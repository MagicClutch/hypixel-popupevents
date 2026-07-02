package de.hypixel.popupevents.client.chat

import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import de.hypixel.popupevents.client.config.ConfigManager
import de.hypixel.popupevents.client.model.PopupRequest
import de.hypixel.popupevents.client.model.RequestType
import de.hypixel.popupevents.client.util.DebugLogger
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import java.util.Locale

object ChatComponentParser {
    private val tradeNamePatterns = listOf(
        Regex("""(?:\[[^\]]+]\s*)?([A-Za-z0-9_]{1,16}) has sent you a trade request\b"""),
        Regex("""(?:\[[^\]]+]\s*)?([A-Za-z0-9_]{1,16}) sent you a trade request\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) has sent you a trade request\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) sent you a trade request\b""")
    )
    private val partyNamePatterns = listOf(
        Regex("""(?:\[[^\]]+]\s*)?([A-Za-z0-9_]{1,16}) has invited you to join (?:their|the) party\b"""),
        Regex("""(?:\[[^\]]+]\s*)?([A-Za-z0-9_]{1,16}) invited you to (?:their|the) party\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) invited you to (?:their|the) party\b"""),
        Regex("""^Party invite from ([A-Za-z0-9_]{1,16})\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) has invited you to join (?:their|the) party\b""")
    )
    private val friendNamePatterns = listOf(
        Regex("""^Friend request from ([A-Za-z0-9_]{1,16})\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) sent you a friend request\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) has sent you a friend request\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) wants to be your friend\b""")
    )
    private val guildNamePatterns = listOf(
        Regex("""(?:\[[^\]]+]\s*)?([A-Za-z0-9_]{1,16}) has invited you to join (?:their|a|the) guild\b"""),
        Regex("""(?:\[[^\]]+]\s*)?([A-Za-z0-9_]{1,16}) invited you to (?:their|a|the) guild\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) invited you to (?:their|a|the) guild\b"""),
        Regex("""^Guild invite from ([A-Za-z0-9_]{1,16})\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) has invited you to join (?:their|a|the) guild\b"""),
        Regex("""^You have been invited to join .+ by ([A-Za-z0-9_]{1,16})\b""")
    )
    private val duelNamePatterns = listOf(
        Regex("""(?:\[[^\]]+]\s*)?([A-Za-z0-9_]{1,16}) has invited you to .+\bDuels?\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) has challenged you to a duel\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) challenged you to a duel\b"""),
        Regex("""^Duel request from ([A-Za-z0-9_]{1,16})\b"""),
        Regex("""^([A-Za-z0-9_]{1,16}) has invited you to a duel\b""")
    )

    fun parse(component: Component): PopupRequest? {
        val flatText = normalizeMessageText(component.string)
        val commands = ArrayList<String>(2)
        collectCommands(component, commands)

        if (ConfigManager.config.debugLogging) {
            DebugLogger.debug("Raw Text component: $component")
            if (ConfigManager.config.printJson) {
                DebugLogger.debug("JSON component: ${toJson(component)}")
            }
        }

        val lowered = flatText.lowercase(Locale.ROOT)
        val tradeCommand = commands.firstCommand("/tradeaccept ", "/trade accept ")
        val partyAcceptCommand = commands.firstCommandWhere { command ->
            command.startsWith("/party accept ", ignoreCase = true) ||
                command.startsWith("/p accept ", ignoreCase = true) ||
                (command.startsWith("/p ", ignoreCase = true) && !command.startsWithAny("/p deny ", "/p decline ", "/p reject "))
        }
        val friendAcceptCommand = commands.firstCommand("/friend accept ", "/f accept ")
        val friendDeclineCommand = commands.firstCommand("/friend deny ", "/friend decline ", "/friend reject ", "/f deny ", "/f decline ")
        val guildAcceptCommand = commands.firstCommand("/guild accept ", "/g accept ")
            ?: extractCommandFromText(flatText, "/guild accept ", "/g accept ")
        val duelAcceptCommand = commands.firstCommand("/duel accept ")

        val request = when {
            ConfigManager.config.tradePopups && lowered.contains("trade request") -> {
                val player = extractPlayer(flatText, tradeNamePatterns) ?: return null
                PopupRequest(RequestType.TRADE, player, tradeCommand ?: "/trade accept $player", null)
            }

            ConfigManager.config.partyPopups && lowered.contains("party") && lowered.contains("invite") -> {
                val player = extractPlayer(flatText, partyNamePatterns) ?: extractPlayerFromPartyCommand(partyAcceptCommand) ?: return null
                PopupRequest(
                    RequestType.PARTY,
                    player,
                    partyAcceptCommand ?: "/p $player",
                    null
                )
            }

            ConfigManager.config.friendPopups && lowered.contains("friend request") -> {
                val player = extractPlayer(flatText, friendNamePatterns) ?: extractPlayerFromCommand(friendAcceptCommand) ?: return null
                PopupRequest(
                    RequestType.FRIEND,
                    player,
                    friendAcceptCommand ?: "/friend accept $player",
                    friendDeclineCommand ?: "/friend deny $player"
                )
            }

            ConfigManager.config.guildPopups && lowered.contains("guild") && lowered.contains("invite") -> {
                val player = extractPlayer(flatText, guildNamePatterns) ?: extractPlayerFromCommand(guildAcceptCommand) ?: return null
                PopupRequest(
                    RequestType.GUILD,
                    player,
                    guildAcceptCommand ?: "/guild accept $player",
                    null
                )
            }

            ConfigManager.config.duelPopups && (lowered.contains("duel request") || lowered.contains("duels") || lowered.contains("challenged you to a duel") || lowered.contains("invited you to a duel")) -> {
                val player = extractPlayer(flatText, duelNamePatterns) ?: extractPlayerFromCommand(duelAcceptCommand) ?: return null
                PopupRequest(
                    RequestType.DUEL,
                    player,
                    duelAcceptCommand ?: "/duel accept $player",
                    null
                )
            }

            else -> null
        }

        if (request != null && ConfigManager.config.debugLogging) {
            DebugLogger.debug("Detected request type: ${request.type}")
            DebugLogger.debug("Player name: ${request.playerName}")
            if (ConfigManager.config.printCommands) {
                DebugLogger.debug("Accept command: ${request.acceptCommand ?: "<none>"}")
                DebugLogger.debug("Decline command: ${request.declineCommand ?: "<none>"}")
            }
        }

        return request
    }

    private fun collectCommands(component: Component, commands: MutableList<String>) {
        collectStyleCommands(component.style, commands)
        for (sibling in component.siblings) {
            collectCommands(sibling, commands)
        }
    }

    private fun collectStyleCommands(style: Style, commands: MutableList<String>) {
        val click = style.clickEvent
        if (click is ClickEvent.RunCommand) {
            commands += click.command
        }

        val hover = style.hoverEvent
        if (hover is HoverEvent.ShowText) {
            collectCommands(hover.value, commands)
        }
    }

    private fun extractPlayer(text: String, patterns: List<Regex>): String? {
        return patterns.firstNotNullOfOrNull { pattern ->
            pattern.find(text)?.groupValues?.getOrNull(1)
        }
    }

    private fun normalizeMessageText(text: String): String {
        return text
            .lines()
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filterNot { line -> line.length >= 8 && line.all { it == '-' } }
            .joinToString("\n")
            .trim()
    }

    private fun extractPlayerFromPartyCommand(command: String?): String? {
        return extractPlayerFromCommand(command)
    }

    private fun extractPlayerFromCommand(command: String?): String? {
        if (command == null) return null
        return command.split(Regex("""\s+""")).lastOrNull()?.takeIf { it.matches(Regex("""[A-Za-z0-9_]{1,16}""")) }
    }

    private fun extractCommandFromText(text: String, vararg prefixes: String): String? {
        val flattened = text.replace('\n', ' ')
        return prefixes.firstNotNullOfOrNull { prefix ->
            val pattern = Regex("""${Regex.escape(prefix)}([A-Za-z0-9_]{1,16})\b""", RegexOption.IGNORE_CASE)
            pattern.find(flattened)?.groupValues?.getOrNull(1)?.let { player -> "$prefix$player" }
        }
    }

    private fun List<String>.firstCommand(vararg prefixes: String): String? {
        return firstOrNull { command ->
            prefixes.any { prefix -> command.startsWith(prefix, ignoreCase = true) }
        }
    }

    private fun List<String>.firstCommandWhere(predicate: (String) -> Boolean): String? {
        return firstOrNull(predicate)
    }

    private fun String.startsWithAny(vararg prefixes: String): Boolean {
        return prefixes.any { prefix -> startsWith(prefix, ignoreCase = true) }
    }

    private fun toJson(component: Component): JsonElement {
        return ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, component).result().orElseThrow()
    }
}
