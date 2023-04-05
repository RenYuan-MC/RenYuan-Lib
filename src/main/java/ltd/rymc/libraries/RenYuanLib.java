package ltd.rymc.libraries;

import ltd.rymc.libraries.managers.LangManager;
import ltd.rymc.libraries.managers.MetricsManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class RenYuanLib extends JavaPlugin {
    public static MetricsManager metricsManager;
    public static RenYuanLib instance;

    @Override
    public void onEnable() {
        instance = this;
        metricsManager = new MetricsManager(this, 17505);
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
}
