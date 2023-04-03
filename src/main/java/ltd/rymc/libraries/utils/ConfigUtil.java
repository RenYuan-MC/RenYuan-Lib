package ltd.rymc.libraries.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ConfigUtil {

    public static FileConfiguration getConfig(String configName, Plugin plugin) {
        File file = new File(plugin.getDataFolder(), configName);
        if (!file.exists()) plugin.saveResource(configName, false);
        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getResourceConfig(String configName, Plugin plugin) {
        InputStream resource = plugin.getResource(configName.replace("\\", "/"));
        return resource == null ? null : YamlConfiguration.loadConfiguration(new InputStreamReader(resource, StandardCharsets.UTF_8));
    }

    public static FileConfiguration newConfig(String configName, Plugin plugin) {
        File file = new File(plugin.getDataFolder(), configName);

        if (!file.exists()) try {
            boolean mkdirs = file.getParentFile().mkdirs();
            boolean newFile = file.createNewFile();
            if (!newFile && !mkdirs) return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public static class Config {
        private final FileConfiguration config;
        private final FileConfiguration resourceConfig;
        private final Plugin plugin;

        public Config(String configName, Plugin plugin) {
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


}
