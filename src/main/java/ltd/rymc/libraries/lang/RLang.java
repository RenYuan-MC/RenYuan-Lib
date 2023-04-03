package ltd.rymc.libraries.lang;

import ltd.rymc.libraries.RenYuanLib;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;

public class RLang {

    private static final RenYuanLib instance = RenYuanLib.instance;

    private final String langName;
    private final FileConfiguration langConfig;
    private final FileConfiguration langResourceConfig;
    private final boolean initState;
    private final HashMap<String, String> keyHashMap = new HashMap<>();

    public RLang(FileConfiguration langConfig, FileConfiguration langResourceConfig, String langName) {
        this.langName = langName;
        this.langConfig = langConfig;
        this.langResourceConfig = langResourceConfig;
        this.initState = init();
        if (!initState) instance.getLogger().severe("Invalid language file for " + langName + " !");
    }

    private boolean init() {
        if (langConfig == null || langResourceConfig == null) return false;
        ConfigurationSection langSection = langResourceConfig.getConfigurationSection("lang");
        if (langSection == null) return false;
        for (String key : langSection.getKeys(false)) {
            String message = langConfig.getString(key,langResourceConfig.getString(key));
            keyHashMap.put(key,message);
        }
        return true;
    }

    public String format(String key) {
        if (!initState) return null;
        return keyHashMap.get(key);
    }

    public String getLangName() {
        return langName;
    }
}
