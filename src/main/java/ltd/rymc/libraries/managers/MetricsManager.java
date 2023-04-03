package ltd.rymc.libraries.managers;

import ltd.rymc.libraries.RenYuanLib;

import ltd.rymc.libraries.metrics.Metrics;
import ltd.rymc.libraries.utils.ConfigUtil;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class MetricsManager {

    private static final boolean metricsStatus = (new ConfigUtil.Config("config.yml", RenYuanLib.instance)).getBoolean("metrics-global");
    private static final HashMap<Plugin, MetricsManager> managerHashMap = new HashMap<>();
    private Metrics metrics = null;
    private final int serviceId;
    private final JavaPlugin plugin;

    public MetricsManager(JavaPlugin plugin, int serviceId) {
        this.plugin = plugin;
        this.serviceId = serviceId;
        managerHashMap.put(plugin, this);
        if (!metricsStatus) return;
        if (!new ConfigUtil.Config("config.yml", plugin).getBoolean("metrics")) return;
        this.metrics = new Metrics(plugin, serviceId);
    }

    public static MetricsManager getMetricsManager(Plugin plugin) {
        return managerHashMap.get(plugin);
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public int getServiceId(){
        return serviceId;
    }
    public JavaPlugin getJavaPlugin(){
        return plugin;
    }
}
