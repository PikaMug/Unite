package me.pikamug.unite.api.objects;

import me.pikamug.unite.api.interfaces.PartyPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public abstract class PartyProvider implements PartyPlugin {
    protected static final Logger log = Logger.getLogger("Minecraft");
    protected Plugin plugin = null;

    public boolean createParty(Player player) {
        return createParty(String.valueOf(System.currentTimeMillis()), player.getUniqueId());
    }

    public boolean isPlayerInParty(Player player) {
        return isPlayerInParty(player.getUniqueId());
    }

    public boolean areInSameParty(Player player1, Player player2) {
        return areInSameParty(player1.getUniqueId(), player2.getUniqueId());
    }
}
