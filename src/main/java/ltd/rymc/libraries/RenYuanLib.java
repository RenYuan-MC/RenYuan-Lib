package ltd.rymc.libraries;

import ltd.rymc.libraries.managers.LangManager;
import ltd.rymc.libraries.managers.MetricsManager;
import ltd.rymc.libraries.utils.CommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RenYuanLib extends JavaPlugin {
    private static final List<Plugin> registeredPluginList = new ArrayList<>();
    public static LangManager langManager;
    public static MetricsManager metricsManager;
    public static RenYuanLib instance;

    public static RenYuanLib getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        registerLangManager(this);
        langManager = getLangManager(this);
        registerMetricsManager(this, 17505);
        metricsManager = getMetricsManager(this);
        PluginCommand command = CommandUtil.getCommand(this, "rylib");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }

    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rylib.admin")) return false;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("plugin-list")) {

                sender.sendMessage(langManager.format(sender, "message-registered-plugin"));
                if (registeredPluginList.size() == 0) sender.sendMessage(langManager.format(sender, "message-nothing"));
                for (Plugin plugin : registeredPluginList)
                    sender.sendMessage(plugin.getName() + " - " + plugin.getDescription().getVersion());

            } else if (args[0].equalsIgnoreCase("reload")) {
                reloadAllManagers(this);

            }
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("set-lang")) {

            if (sender instanceof Player) {
                langManager.setPlayerLang((Player) sender, args[1]);
            } else sender.sendMessage(langManager.format(sender, "message-only-player"));

        }

        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!sender.hasPermission("rylib.admin")) return new ArrayList<>();

        if (args.length == 1) return Arrays.asList("plugin-list", "set-lang", "reload");

        if (args.length == 2 && args[0].equalsIgnoreCase("set-lang")) return langManager.getLangList();

        return new ArrayList<>();
    }




    public void registerPlugin(Plugin plugin) {
        if (plugin.equals(instance)) return;
        if (!registeredPluginList.contains(plugin)) registeredPluginList.add(plugin);
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

    public void registerLangManager(Plugin plugin) {
        new LangManager(plugin);
    }

    public void registerMetricsManager(JavaPlugin plugin, int serviceId) {
        new MetricsManager(plugin, serviceId);
    }

    public void reloadAllManagers(Plugin plugin) {
        reloadMetricsManager(plugin);
        reloadLangManager(plugin);
    }
}
