package ltd.rymc.libraries.managers;

import ltd.rymc.libraries.RenYuanLib;
import ltd.rymc.libraries.config.RConfig;
import ltd.rymc.libraries.lang.RLang;
import ltd.rymc.libraries.utils.ConfigUtil;
import ltd.rymc.libraries.utils.StorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LangManager {
    private static final HashMap<Plugin, LangManager> managerHashMap = new HashMap<>();
    private static final RenYuanLib instance = RenYuanLib.instance;
    private final Plugin plugin;
    private final List<RLang> langList = new ArrayList<>();
    private final HashMap<UUID, RLang> playerLangMap = new HashMap<>();
    private final LangListener listener;
    private FileConfiguration builtInLang = null;
    private RLang consoleLang;

    public LangManager(Plugin plugin) {
        // 注册插件
        this.plugin = plugin;
        instance.registerPlugin(plugin);

        // 获取原来的Manager并关闭原Manager的缓存
        LangManager originalManager = managerHashMap.get(plugin);
        if (originalManager != null) originalManager.disableCache();

        // 放入新的Manager
        managerHashMap.put(plugin, this);

        // 初始化缓存监听器
        listener = new LangListener();

        // 初始化
        initLang();

        // 启用缓存
        for (Player player : Bukkit.getOnlinePlayers()) loadPlayerToCache(player.getUniqueId());
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static LangManager getLangManager(Plugin plugin) {
        return managerHashMap.get(plugin);
    }

    public String format(CommandSender sender, String key) {
        // 判断输入类型并选取语言
        RLang lang = sender instanceof Player ? getPlayerLang(((Player) sender).getUniqueId()) : consoleLang;

        // 如语言为null则返回内置语言
        if (lang == null) return builtInLang.getString(key, key);

        // 获取对应信息并返回
        String format = lang.format(key);
        return format == null ? key : format;

    }

    public String formatForConsole(String key) {

        // 如语言为null则返回内置语言
        if (consoleLang == null) return builtInLang.getString(key, key);

        // 获取对应信息并返回
        String format = consoleLang.format(key);
        return format == null ? key : format;

    }

    public List<String> getLangList() {
        List<String> list = new ArrayList<>();
        for (RLang lang : langList) list.add(lang.getLangName());
        return list;
    }

    public void setPlayerLang(Player player, String langName) {
        // 获取语言
        RLang lang = getLang(langName);
        if (lang == null) return;

        // 保存语言并加载到缓存
        StorageUtil.setPrivatePlayerData(player.getUniqueId(), "lang", langName, plugin);
        playerLangMap.put(player.getUniqueId(), lang);

    }

    public RLang getPlayerLang(UUID player) {
        // 从缓存读取玩家语言
        RLang lang = playerLangMap.get(player);
        if (lang != null) return lang;

        // 若缓存没有则从数据文件加载
        lang = getLang(StorageUtil.getPrivatePlayerDataString(player, "lang", plugin));
        playerLangMap.put(player, lang);

        return lang;
    }

    private void initLang() {

        builtInLang = ConfigUtil.getResourceConfig("lang\\_default.yml", plugin);
        RConfig langListConfig = new RConfig("lang.yml", plugin);
        List<String> list = langListConfig.getStringList("lang");
        for (String langName : list)
            if (!langName.startsWith("_")) {
                RConfig langConfig = new RConfig("lang\\" + langName + ".yml", plugin);
                langList.add(new RLang(langConfig.getConfig(), langConfig.getResourceConfig(), langName));
            }
        consoleLang = getLang(langListConfig.getString("console-lang"));
    }

    private void disableCache() {
        HandlerList.unregisterAll(listener);
    }

    private void loadPlayerToCache(UUID player) {
        String language = StorageUtil.getPrivatePlayerDataString(player, "lang", plugin);
        if (language == null) language = langList.get(0).getLangName();
        if (language == null) return;
        RLang lang = getLang(language);
        playerLangMap.put(player, lang);
    }

    private RLang getLang(String langName) {
        for (RLang lang : langList) if (lang.getLangName().equals(langName)) return lang;
        return null;
    }

    private class LangListener implements Listener {
        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onPlayerLogin(PlayerLoginEvent event) {
            loadPlayerToCache(event.getPlayer().getUniqueId());
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            playerLangMap.remove(event.getPlayer().getUniqueId());
        }

    }

}
