package me.pikamug.unite.api.objects.plugins;

import de.simonsator.partyandfriends.spigot.api.events.BridgeNotAvailableException;
import de.simonsator.partyandfriends.spigot.api.events.PartyEventListenerInterface;
import de.simonsator.partyandfriends.spigot.api.events.PartyEventManager;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import de.simonsator.partyandfriendsgui.api.PartyFriendsAPI;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.events.pafgui.PartyCreateEvent_PAFGUI;
import me.pikamug.unite.api.events.pafgui.PartyDeleteEvent_PAFGUI;
import me.pikamug.unite.api.events.pafgui.PartyJoinEvent_PAFGUI;
import me.pikamug.unite.api.events.pafgui.PartyLeaveEvent_PAFGUI;
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

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PartyProvider_PAFGUI extends PartyProvider {
    private final String pluginName = "PartyAndFriendsGUI";

    public PartyProvider_PAFGUI(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new PartyProvider_PAFGUI.PartyServerListener(this), plugin);
        if (PartyEventManager.isBridgeAvailable()) {
            try {
                PartyEventManager.registerPartyEventListener(new PartyServerListenerInterface(this));
            } catch (BridgeNotAvailableException e) {
                e.printStackTrace();
            }
        } else {
            plugin.getLogger().severe("§cThe Party And Friends main plugin is not installed. Download it from https://www.spigotmc.org/resources/10123/");
        }
    }

    public class PartyServerListener implements Listener {
        final PartyProvider_PAFGUI partyPlugin;

        public PartyServerListener(PartyProvider_PAFGUI partyPlugin) {
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

    }

    public class PartyServerListenerInterface implements PartyEventListenerInterface {
        final PartyProvider_PAFGUI partyPlugin;

        public PartyServerListenerInterface(PartyProvider_PAFGUI partyPlugin) {
            this.partyPlugin = partyPlugin;
        }

        @Override
        public void onPartyCreated(@Nullable PlayerParty playerParty) {
            if (playerParty == null) {
                return;
            }
            final PartyCreateEvent event = new PartyCreateEvent_PAFGUI(partyPlugin,
                    playerParty.getLeader().getUniqueId(), false);
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @Override
        public void onPartyJoin(PAFPlayer pafPlayer, @Nullable PlayerParty playerParty) {
            if (playerParty == null) {
                return;
            }
            final PartyJoinEvent event = new PartyJoinEvent_PAFGUI(partyPlugin, pafPlayer.getUniqueId(), false);
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @Override
        public void onLeftParty(PAFPlayer pafPlayer, @Nullable PlayerParty playerParty) {
            if (playerParty == null) {
                return;
            }
            final PartyLeaveEvent event = new PartyLeaveEvent_PAFGUI(partyPlugin, pafPlayer.getUniqueId(), false);
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @Override
        public void onPartyLeaderChanged(PAFPlayer pafPlayer, @Nullable PlayerParty playerParty) {
            if (playerParty == null) {
                return;
            }
            if (playerParty.getAllPlayers().isEmpty()) {
                final PartyDeleteEvent event = new PartyDeleteEvent_PAFGUI(partyPlugin, pafPlayer.getUniqueId(), false);
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
        if (partyName != null) {
            // PAFGUI cannot create parties, so just invite first var
            PartyFriendsAPI.inviteIntoParty(player, partyName);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isPlayerInParty(UUID playerId) {
        PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(playerId);
        return PartyManager.getInstance().getParty(pafPlayer) != null;
    }

    @Override
    public boolean areInSameParty(UUID playerId1, UUID playerId2) {
        PAFPlayer pafPlayer1 = PAFPlayerManager.getInstance().getPlayer(playerId1);
        PAFPlayer pafPlayer2 = PAFPlayerManager.getInstance().getPlayer(playerId2);
        if (PartyManager.getInstance().getParty(pafPlayer1) != null
                && PartyManager.getInstance().getParty(pafPlayer2) != null) {
            return PartyManager.getInstance().getParty(pafPlayer1).isInParty(pafPlayer2);
        }
        return false;
    }

    @Override
    public String getPartyName(UUID playerId) {
        PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(playerId);
        // Return leader name since PAFGUI does not use party names
        if (PartyManager.getInstance().getParty(pafPlayer) != null) {
            return PartyManager.getInstance().getParty(pafPlayer).getLeader().getName();
        }
        return null;
    }

    @Override
    public @Nullable String getPartyId(UUID playerId) {
        PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(playerId);
        // Return leader UUID since PAFGUI does not use party IDs
        if (PartyManager.getInstance().getParty(pafPlayer) != null) {
            return PartyManager.getInstance().getParty(pafPlayer).getLeader().getUniqueId().toString();
        }
        return null;
    }

    @Override
    public UUID getLeader(String partyId) {
        // Lookup player UUID since PAFGUI does not use party IDs
        PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(UUID.fromString(partyId));
        if (pafPlayer != null && PartyManager.getInstance().getParty(pafPlayer) != null) {
            return PartyManager.getInstance().getParty(pafPlayer).getLeader().getUniqueId();
        }
        return null;
    }

    @Override
    public Set<UUID> getMembers(String partyId) {
        // Return online members since PAFGUI does not retain offline
        return getOnlineMembers(partyId);
    }

    @Override
    public Set<UUID> getOnlineMembers(String partyId) {
        PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(UUID.fromString(partyId));
        if (pafPlayer != null) {
            return PartyManager.getInstance().getParty(pafPlayer).getAllPlayers().stream().map(PAFPlayer::getUniqueId).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
