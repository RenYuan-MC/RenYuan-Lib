package ltd.rymc.libraries.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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


}
