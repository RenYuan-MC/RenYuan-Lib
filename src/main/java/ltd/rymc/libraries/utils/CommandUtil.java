package ltd.rymc.libraries.utils;

import ltd.rymc.libraries.RenYuanLib;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandUtil {
    public static PluginCommand getCommand(JavaPlugin plugin, String commandName) {
        PluginCommand command = plugin.getCommand(commandName);
        if (command != null) return command;

        plugin.getLogger().severe(RenYuanLib.langManager.formatForConsole("console-command-error"));

        return null;
    }
}
