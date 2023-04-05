package ltd.rymc.libraries.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

public class PlayerUtil {

    public static String[] getOnlinePlayerNameList() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        String[] playerNames = new String[onlinePlayers.size()];
        int i = 0;
        for (Player player : onlinePlayers) playerNames[i++] = player.getName();
        return playerNames;
    }

    public static String[] getOnlinePlayerNameListExact() {
        String[] originalNames = getOnlinePlayerNameList(), names = new String[originalNames.length + 1];
        names[0] = "";
        int j = 0;
        for(int l = originalNames.length; j < l; ++j) names[j + 1] = originalNames[j];
        return names;
    }

    public static void sendMessage(CommandSender sender,String message,String prefix){
        sender.sendMessage((sender instanceof Player ? prefix : "") + message);
    }

    public static String[] arraysFilter(String[] arr,String fil) {
        return Arrays.stream(arr).filter(s -> !fil.equals(s)).toArray(String[]::new);
    }
}
