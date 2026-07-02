package de.hypixel.popupevents.client.model

data class PopupRequest(
    val type: RequestType,
    val playerName: String,
    val acceptCommand: String?,
    val declineCommand: String?,
    val receivedAtMillis: Long = System.currentTimeMillis()
) {
    val title: String
        get() = when (type) {
            RequestType.TRADE -> "$playerName sent you a trade request."
            RequestType.PARTY -> "$playerName invited you to their party."
            RequestType.FRIEND -> "$playerName sent you a friend request."
            RequestType.GUILD -> "$playerName sent you a guild invite."
            RequestType.DUEL -> "$playerName challenged you to a duel."
        }

    val question: String
        get() = when (type) {
            RequestType.TRADE -> "Accept this trade request?"
            RequestType.PARTY -> "Accept this party invite?"
            RequestType.FRIEND -> "Accept this friend request?"
            RequestType.GUILD -> "Accept this guild invite?"
            RequestType.DUEL -> "Accept this duel request?"
        }
}
