package me.pikamug.unite.api.objects.plugins;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.events.party.McMMOPartyChangeEvent;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.player.UserManager;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.events.mcmmo.PartyCreateEvent_mcMMO;
import me.pikamug.unite.api.events.mcmmo.PartyDeleteEvent_mcMMO;
import me.pikamug.unite.api.events.mcmmo.PartyJoinEvent_mcMMO;
import me.pikamug.unite.api.events.mcmmo.PartyLeaveEvent_mcMMO;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PartyProvider_mcMMO extends PartyProvider {
    private final String pluginName = "mcMMO";

    public PartyProvider_mcMMO(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new PartyServerListener(this), plugin);
    }

    public class PartyServerListener implements Listener {
        final PartyProvider_mcMMO partyPlugin;

        public PartyServerListener(PartyProvider_mcMMO partyPlugin) {
            this.partyPlugin = partyPlugin;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            if (event.getPlugin().getDescription().getName().equals(pluginName)) {
                log.info(String.format("[%s][Party] %s hooked.", plugin.getDescription().getName(), partyPlugin.pluginName));
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin().getDescription().getName().equals(pluginName)) {
                log.info(String.format("[%s][Party] %s un-hooked.", plugin.getDescription().getName(), partyPlugin.pluginName));
            }
        }

        @EventHandler
        public void onPartyCreate(final McMMOPartyChangeEvent pluginEvent) {
            if (pluginEvent.getReason().equals(McMMOPartyChangeEvent.EventReason.CREATED_PARTY)) {
                final PartyCreateEvent event = new PartyCreateEvent_mcMMO(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
                plugin.getServer().getPluginManager().callEvent(event);
            }
        }

        @EventHandler
        public void onPartyDelete(final McMMOPartyChangeEvent pluginEvent) {
            if (pluginEvent.getReason().equals(McMMOPartyChangeEvent.EventReason.DISBANDED_PARTY)) {
                final PartyDeleteEvent event = new PartyDeleteEvent_mcMMO(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
                plugin.getServer().getPluginManager().callEvent(event);
            }
        }

        @EventHandler
        public void onPartyJoin(final McMMOPartyChangeEvent pluginEvent) {
            if (pluginEvent.getReason().equals(McMMOPartyChangeEvent.EventReason.JOINED_PARTY)) {
                final PartyJoinEvent event = new PartyJoinEvent_mcMMO(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
                plugin.getServer().getPluginManager().callEvent(event);
            }
        }

        @EventHandler
        public void onPartyLeave(final McMMOPartyChangeEvent pluginEvent) {
            if (pluginEvent.getReason().equals(McMMOPartyChangeEvent.EventReason.LEFT_PARTY)) {
                final PartyLeaveEvent event = new PartyLeaveEvent_mcMMO(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
                plugin.getServer().getPluginManager().callEvent(event);
            }
        }
    }

    @Override
    public boolean isPluginEnabled() {
        if (plugin.getServer().getPluginManager().getPlugin(pluginName) != null) {
            return Objects.requireNonNull(plugin.getServer().getPluginManager().getPlugin(pluginName)).isEnabled();
        }
        return false;
    }

    @Override
    public @NotNull String getPluginName() {
        return pluginName;
    }

    @Override
    public boolean createParty(String partyName, UUID playerId) {
        final Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            return false;
        }
        final PlayerProfile profile = UserManager.getPlayer(player).getProfile();
        final McMMOPlayer mcMMOPlayer = new McMMOPlayer(player, profile);
        PartyManager.createParty(mcMMOPlayer, partyName, null);
        return true;
    }

    @Override
    public boolean isPlayerInParty(UUID playerId) {
        return PartyManager.getParty(Bukkit.getPlayer(playerId)) != null;
    }

    @Override
    public boolean areInSameParty(UUID playerId1, UUID playerId2) {
        return PartyManager.inSameParty(Bukkit.getPlayer(playerId1), Bukkit.getPlayer(playerId2));
    }

    @Override
    public String getPartyName(UUID playerId) {
        if (PartyManager.getParty(Bukkit.getPlayer(playerId)) != null) {
            return PartyManager.getParty(Bukkit.getPlayer(playerId)).getName();
        }
        return null;
    }

    @Override
    public @Nullable String getPartyId(UUID playerId) {
        // Return name since mcMMO does not use party IDs
        return getPartyName(playerId);
    }

    @Override
    public UUID getLeader(String partyId) {
        //final String playerName = PartyManager.getPartyLeaderName(partyId);
        //return Bukkit.getOfflinePlayer(playerName).getUniqueId();
        return PartyManager.getParty(partyId).getLeader().getUniqueId();
    }

    @Override
    public Set<UUID> getMembers(String partyId) {
        final UUID playerId = getLeader(partyId);
        return PartyManager.getAllMembers(Bukkit.getPlayer(playerId)).keySet();
    }

    @Override
    public Set<UUID> getOnlineMembers(String partyId) {
        return PartyManager.getOnlineMembers(partyId).stream().map(Player::getUniqueId).collect(Collectors.toSet());
    }
}
