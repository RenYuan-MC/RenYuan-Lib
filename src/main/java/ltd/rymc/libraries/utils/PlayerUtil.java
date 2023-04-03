package ltd.rymc.libraries.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class PlayerUtil {

    public static String[] getOnlinePlayerNameList() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        String[] playerNames = new String[onlinePlayers.size() + 1];
        playerNames[0] = "";
        int i = 1;
        for (Player player : onlinePlayers) {
            playerNames[i++] = player.getName();
        }
        return playerNames;
    }

    public static String[] getOnlinePlayerNameListExact() {
        String[] originalNames = getOnlinePlayerNameList();
        String[] names = new String[originalNames.length + 1];
        names[0] = "";

        int j = 0;
        for(int l = originalNames.length; j < l; ++j) names[j + 1] = originalNames[j];

        return names;
    }
}
