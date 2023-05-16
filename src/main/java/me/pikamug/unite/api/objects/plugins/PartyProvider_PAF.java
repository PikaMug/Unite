package me.pikamug.unite.api.objects.plugins;

import de.simonsator.partyandfriends.api.events.party.LeftPartyEvent;
import de.simonsator.partyandfriends.api.events.party.PartyCreatedEvent;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyManager;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.events.paf.PartyCreateEvent_PAF;
import me.pikamug.unite.api.events.paf.PartyDeleteEvent_PAF;
import me.pikamug.unite.api.events.paf.PartyJoinEvent_PAF;
import me.pikamug.unite.api.events.paf.PartyLeaveEvent_PAF;
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

public class PartyProvider_PAF extends PartyProvider {
    private final String pluginName = "PartyAndFriends";

    public PartyProvider_PAF(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new PartyProvider_PAF.PartyServerListener(this), plugin);
    }

    public class PartyServerListener implements Listener {
        final PartyProvider_PAF partyPlugin;

        public PartyServerListener(PartyProvider_PAF partyPlugin) {
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
        public void onPartyCreate(final PartyCreatedEvent pluginEvent) {
            final PartyCreateEvent event = new PartyCreateEvent_PAF(partyPlugin, pluginEvent, false);
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyDelete(final LeftPartyEvent pluginEvent) {
            if (pluginEvent.getParty().getAllPlayers().isEmpty()) {
                final PartyDeleteEvent event = new PartyDeleteEvent_PAF(partyPlugin, pluginEvent, false);
                plugin.getServer().getPluginManager().callEvent(event);
            }
        }

        @EventHandler
        public void onPartyJoin(final de.simonsator.partyandfriends.api.events.party.PartyJoinEvent pluginEvent) {
            final PartyJoinEvent event = new PartyJoinEvent_PAF(partyPlugin, pluginEvent, false);
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyLeave(final LeftPartyEvent pluginEvent) {
            final PartyLeaveEvent event = new PartyLeaveEvent_PAF(partyPlugin, pluginEvent, false);
            plugin.getServer().getPluginManager().callEvent(event);
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
        PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(player.getUniqueId());
        if (pafPlayer.isOnline()) {
            PartyManager.getInstance().createParty((OnlinePAFPlayer) pafPlayer);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isPlayerInParty(UUID playerId) {
        return PartyManager.getInstance().getParty(playerId) != null;
    }

    @Override
    public boolean areInSameParty(UUID playerId1, UUID playerId2) {
        PAFPlayer pafPlayer1 = PAFPlayerManager.getInstance().getPlayer(playerId1);
        PAFPlayer pafPlayer2 = PAFPlayerManager.getInstance().getPlayer(playerId2);
        if (!(pafPlayer1.isOnline() && pafPlayer2.isOnline())) {
            return false;
        }
        if (PartyManager.getInstance().getParty(playerId1) != null
                && PartyManager.getInstance().getParty(playerId2) != null) {
            return PartyManager.getInstance().getParty(playerId1).isInParty((OnlinePAFPlayer) pafPlayer2);
        }
        return false;
    }

    @Override
    public String getPartyName(UUID playerId) {
        // Return leader name since PAF does not use party names
        if (PartyManager.getInstance().getParty(playerId) != null) {
            return PartyManager.getInstance().getParty(playerId).getLeader().getName();
        }
        return null;
    }

    @Override
    public @Nullable String getPartyId(UUID playerId) {
        // Return leader UUID since PAF does not use party IDs
        if (PartyManager.getInstance().getParty(playerId) != null) {
            return PartyManager.getInstance().getParty(playerId).getLeader().getUniqueId().toString();
        }
        return null;
    }

    @Override
    public UUID getLeader(String partyId) {
        // Lookup player UUID since PAF does not use party IDs
        if (PartyManager.getInstance().getParty(UUID.fromString(partyId)) != null) {
            return PartyManager.getInstance().getParty(UUID.fromString(partyId)).getLeader().getUniqueId();
        }
        return null;
    }

    @Override
    public Set<UUID> getMembers(String partyId) {
        // Return online members since PAF does not retain offline
        return getOnlineMembers(partyId);
    }

    @Override
    public Set<UUID> getOnlineMembers(String partyId) {
        final UUID playerId = getLeader(partyId);
        if (playerId != null) {
            return PartyManager.getInstance().getParty(playerId).getAllPlayers().stream().map(OnlinePAFPlayer::getUniqueId).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
