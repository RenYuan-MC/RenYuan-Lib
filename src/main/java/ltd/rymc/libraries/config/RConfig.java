package ltd.rymc.libraries.config;

import ltd.rymc.libraries.RenYuanLib;
import ltd.rymc.libraries.utils.ConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class RConfig {
    private final FileConfiguration config;
    private final FileConfiguration resourceConfig;
    private final Plugin plugin;

    public RConfig(String configName, Plugin plugin) {
        RenYuanLib.getInstance().registerPlugin(plugin);
        this.plugin = plugin;
        this.config = ConfigUtil.getConfig(configName, plugin);
        this.resourceConfig = ConfigUtil.getResourceConfig(configName.replace("\\", "/"), plugin);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getResourceConfig() {
        return resourceConfig;
    }

    public List<String> getStringList(String path) {
        List<String> stringList = config.getStringList(path);
        if (stringList.size() > 0) return stringList;
        return resourceConfig.getStringList(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path, resourceConfig.getBoolean(path));
    }

    public String getString(String path) {
        return config.getString(path, resourceConfig.getString(path));
    }

    public int getInt(String path) {
        return config.getInt(path, resourceConfig.getInt(path));
    }

    public double getDouble(String path) {
        return config.getDouble(path, resourceConfig.getDouble(path));
    }

    public long getLong(String path) {
        return config.getLong(path, resourceConfig.getLong(path));
    }
}
