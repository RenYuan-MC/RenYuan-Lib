package ltd.rymc.libraries.managers;

import ltd.rymc.libraries.utils.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class LangManager {
    private static final HashMap<Plugin, LangManager> managerHashMap = new HashMap<>();
    private final HashMap<String, String> keyHashMap = new HashMap<>();

    public LangManager(Plugin plugin) {

        managerHashMap.put(plugin, this);

        String langName = "lang\\" + new ConfigUtil.Config("config.yml", plugin).getString("lang") + ".yml";

        FileConfiguration langConfig = ConfigUtil.getConfig(langName, plugin);
        FileConfiguration langResourceConfig = ConfigUtil.getResourceConfig(langName, plugin);

        if (langResourceConfig == null) return;
        ConfigurationSection langSection = langResourceConfig.getConfigurationSection("lang");
        if (langSection == null) return;
        for (String key : langSection.getKeys(false)) {
            keyHashMap.put(key,langConfig.getString("lang." + key,langResourceConfig.getString("lang." + key)));
        }
    }

    public static LangManager getLangManager(Plugin plugin) {
        return managerHashMap.get(plugin);
    }

    public String get(String key) {
        String message = keyHashMap.get(key);
        return message == null ? key : message;
    }

}
