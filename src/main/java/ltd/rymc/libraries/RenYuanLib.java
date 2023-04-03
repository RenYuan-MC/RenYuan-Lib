package ltd.rymc.libraries;

import ltd.rymc.libraries.managers.LangManager;
import ltd.rymc.libraries.managers.MetricsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RenYuanLib extends JavaPlugin {
    public static MetricsManager metricsManager;
    public static RenYuanLib instance;

    @Override
    public void onEnable() {
        instance = this;
        metricsManager = new MetricsManager(this, 17505);
        PluginCommand command = getCommand("rylib");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }

    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("rylib.admin") && args.length == 1 && args[0].equalsIgnoreCase("reload")) reloadMetricsManager(this);
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender.hasPermission("rylib.admin") && args.length == 1) return Collections.singletonList("reload");
        return new ArrayList<>();
    }

    public LangManager getLangManager(Plugin plugin) {
        return LangManager.getLangManager(plugin);
    }

    public MetricsManager getMetricsManager(Plugin plugin) {
        return MetricsManager.getMetricsManager(plugin);
    }

    public void reloadLangManager(Plugin plugin) {
        if (LangManager.getLangManager(plugin) != null) new LangManager(plugin);
    }

    public void reloadMetricsManager(Plugin plugin) {
        MetricsManager metricsManager = MetricsManager.getMetricsManager(plugin);
        if (metricsManager != null) new MetricsManager(metricsManager.getJavaPlugin(), metricsManager.getServiceId());
    }

    public void reloadAllManagers(Plugin plugin) {
        reloadMetricsManager(plugin);
        reloadLangManager(plugin);
    }
}
