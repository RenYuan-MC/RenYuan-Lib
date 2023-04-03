package ltd.rymc.libraries.utils;

import ltd.rymc.libraries.RenYuanLib;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class StorageUtil {
    private static final HashMap<UUID, FileConfiguration> playerDataMap = new HashMap<>();

    static {
        Bukkit.getPluginManager().registerEvents(new StorageListener(), RenYuanLib.instance);
    }

    public static void setPublicPlayerData(UUID player, String path, String data) {
        if (path.startsWith("_")) return;
        setPlayerDataString(player,path,data,null);
    }

    public static String getPublicPlayerDataString(UUID player, String path) {
        if (path.startsWith("_")) return null;
        return getPlayerDataString(player,path,null);
    }

    private static FileConfiguration getDataConfig(UUID player) {
        FileConfiguration config = playerDataMap.get(player);
        if (config != null) return config;
        return ConfigUtil.newConfig("player-data\\" + player + ".yml", RenYuanLib.instance);
    }

    public static void setPrivatePlayerData(UUID player, String path, String data,Plugin plugin) {
        setPlayerDataString(player,path,data,plugin);
    }

    public static String getPrivatePlayerDataString(UUID player, String path,Plugin plugin) {
        return getPlayerDataString(player,path,plugin);
    }

    private static String getPlayerDataString(UUID player, String path, Plugin plugin){
        String privatePath = plugin != null ? "_" + plugin.getName() + "." : "";
        FileConfiguration dataConfig = getDataConfig(player);
        return dataConfig.getString(privatePath + path);
    }

    private static void setPlayerDataString(UUID player, String path, String data, Plugin plugin){
        String privatePath = plugin != null ? "_" + plugin.getName() + "." : "";
        FileConfiguration dataConfig = getDataConfig(player);

        dataConfig.set(privatePath + path, data);

        try {
            dataConfig.save(new File(RenYuanLib.instance.getDataFolder(),"player-data\\" + player + ".yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class StorageListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onPlayerLogin(PlayerLoginEvent event) {
            UUID uuid = event.getPlayer().getUniqueId();
            playerDataMap.put(uuid, getDataConfig(uuid));
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            playerDataMap.remove(event.getPlayer().getUniqueId());
        }
    }
}
