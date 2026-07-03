package de.hypixel.popupevents.client.config;

import com.teamresourceful.resourcefulconfig.api.annotations.Comment;
import com.teamresourceful.resourcefulconfig.api.annotations.Config;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigButton;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import de.hypixel.popupevents.client.screen.DeferredScreenOpener;

@ConfigInfo(
    title = "Hypixel Request Popups",
    description = "Client-side Hypixel party, trade, friend, guild, and duel request HUD popups."
)
@Config("popupevents")
public final class PopupConfigData {
    @ConfigEntry(id = "enabled", translation = "Enable mod")
    public static boolean enabled = true;

    @ConfigEntry(id = "tradePopups", translation = "Enable trade popups")
    public static boolean tradePopups = true;

    @ConfigEntry(id = "partyPopups", translation = "Enable party popups")
    public static boolean partyPopups = true;

    @ConfigEntry(id = "friendPopups", translation = "Enable friend popups")
    public static boolean friendPopups = true;

    @ConfigEntry(id = "guildPopups", translation = "Enable guild popups")
    public static boolean guildPopups = true;

    @ConfigEntry(id = "duelPopups", translation = "Enable duel popups")
    public static boolean duelPopups = true;

    @ConfigButton(title = "Popup editor", text = "Open Editor")
    @Comment("Drag the popup and use the scroll wheel to resize it.")
    public static final Runnable openEditor = () -> DeferredScreenOpener.INSTANCE.open(PopupPositionEditScreen::new);

    @ConfigOption.Hidden
    @ConfigEntry(id = "x")
    public static int x = -1;

    @ConfigOption.Hidden
    @ConfigEntry(id = "y")
    public static int y = 20;

    @ConfigOption.Hidden
    @ConfigEntry(id = "width")
    public static int width = 260;

    @ConfigOption.Hidden
    @ConfigEntry(id = "height")
    public static int height = 76;

    @ConfigOption.Hidden
    @ConfigEntry(id = "scale")
    public static double scale = 1.0;

    @ConfigOption.Separator(value = "Popup", description = "Display and layout options.")
    @ConfigOption.Slider
    @ConfigOption.Range(min = 1.0, max = 30.0)
    @ConfigEntry(id = "durationSeconds", translation = "Popup duration")
    public static double durationSeconds = 5.0;

    @ConfigOption.Slider
    @ConfigOption.Range(min = 0.0, max = 1.0)
    @ConfigEntry(id = "backgroundOpacity", translation = "Background opacity")
    public static double backgroundOpacity = 0.72;

    @ConfigEntry(id = "backgroundEnabled", translation = "Background enabled")
    public static boolean backgroundEnabled = true;

    @ConfigEntry(id = "textAlignment", translation = "Text alignment")
    @Comment("Controls how the popup title, question, and key hints are aligned.")
    public static PopupTextAlignment textAlignment = PopupTextAlignment.CENTER;

    @ConfigOption.Separator(value = "Animation", description = "Popup transition timing.")
    @ConfigEntry(id = "animations", translation = "Enable animations")
    public static boolean animations = true;

    @ConfigOption.Slider
    @ConfigOption.Range(min = 0.1, max = 4.0)
    @ConfigEntry(id = "animationSpeed", translation = "Animation speed")
    public static double animationSpeed = 1.0;

    @ConfigOption.Slider
    @ConfigOption.Range(min = 0.05, max = 2.0)
    @ConfigEntry(id = "fadeDurationSeconds", translation = "Fade duration")
    public static double fadeDurationSeconds = 0.22;

    @ConfigOption.Slider
    @ConfigOption.Range(min = 0.1, max = 2.0)
    @ConfigEntry(id = "slideUpDurationSeconds", translation = "Slide up duration")
    public static double slideUpDurationSeconds = 0.5;

    @ConfigOption.Separator(value = "Debug", description = "Development logging options.")
    @ConfigEntry(id = "debugLogging", translation = "Enable debug logging")
    public static boolean debugLogging = false;

    @ConfigEntry(id = "printJson", translation = "Print detected JSON chat component")
    public static boolean printJson = false;

    @ConfigEntry(id = "printCommands", translation = "Print extracted commands")
    public static boolean printCommands = false;

    private PopupConfigData() {
    }
}
