package me.pikamug.unite.api.objects.plugins;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostLeaveEvent;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.events.parties.PartyCreateEvent_Parties;
import me.pikamug.unite.api.events.parties.PartyDeleteEvent_Parties;
import me.pikamug.unite.api.events.parties.PartyJoinEvent_Parties;
import me.pikamug.unite.api.events.parties.PartyLeaveEvent_Parties;
import me.pikamug.unite.api.objects.PartyProvider;
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

public class PartyProvider_Parties extends PartyProvider {
    private PartiesAPI parties;
    private final String pluginName = "Parties";

    public PartyProvider_Parties(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new PartyServerListener(this), plugin);

        if (parties == null) {
            parties = Parties.getApi();
        }
    }

    public class PartyServerListener implements Listener {
        final PartyProvider_Parties partyPlugin;

        public PartyServerListener(PartyProvider_Parties partyPlugin) {
            this.partyPlugin = partyPlugin;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            if (partyPlugin.parties == null) {
                if (event.getPlugin().getDescription().getName().equals(pluginName)) {
                    partyPlugin.parties = Parties.getApi();
                    log.info(String.format("[%s][Party] %s hooked.", plugin.getDescription().getName(), partyPlugin.pluginName));
                }
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event) {
            if (partyPlugin.parties != null) {
                if (event.getPlugin().getDescription().getName().equals(pluginName)) {
                    partyPlugin.parties = null;
                    log.info(String.format("[%s][Party] %s un-hooked.", plugin.getDescription().getName(), partyPlugin.pluginName));
                }
            }
        }

        @EventHandler
        public void onPartyCreate(final BukkitPartiesPartyPostCreateEvent pluginEvent) {
            final PartyCreateEvent event = new PartyCreateEvent_Parties(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyDelete(final BukkitPartiesPartyPostDeleteEvent pluginEvent) {
            final PartyDeleteEvent event = new PartyDeleteEvent_Parties(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyJoin(final BukkitPartiesPlayerPostJoinEvent pluginEvent) {
            final PartyJoinEvent event = new PartyJoinEvent_Parties(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyLeave(final BukkitPartiesPlayerPostLeaveEvent pluginEvent) {
            final PartyLeaveEvent event = new PartyLeaveEvent_Parties(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }
    }

    @Override
    public boolean isPluginEnabled() {
        if (parties == null) {
            return false;
        }
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
        return parties.createParty(partyName, parties.getPartyPlayer(playerId));
    }

    @Override
    public boolean isPlayerInParty(UUID playerId) {
        return parties.isPlayerInParty(playerId);
    }

    @Override
    public boolean areInSameParty(UUID playerId1, UUID playerId2) {
        return parties.areInTheSameParty(playerId1, playerId2);
    }

    @Override
    public String getPartyName(UUID playerId) {
        return parties.getPartyPlayer(playerId).getPartyName();
    }

    @Override
    public @Nullable String getPartyId(UUID playerId) {
        if (parties.getPartyPlayer(playerId).getPartyId() != null) {
            return Objects.requireNonNull(parties.getPartyPlayer(playerId).getPartyId()).toString();
        }
        return null;
    }

    @Override
    public UUID getLeader(String partyId) {
        final UUID pid = UUID.fromString(partyId);
        if (parties.getParty(pid) != null) {
            return parties.getParty(pid).getLeader();
        }
        return null;
    }

    @Override
    public Set<UUID> getMembers(String partyId) {
        final UUID pid = UUID.fromString(partyId);
        if (parties.getParty(pid) != null) {
            return parties.getParty(pid).getMembers();
        }
        return Collections.emptySet();
    }

    @Override
    public Set<UUID> getOnlineMembers(String partyId) {
        final UUID pid = UUID.fromString(partyId);
        if (parties.getParty(pid) != null) {
            return parties.getParty(pid).getOnlineMembers(true).stream().map(PartyPlayer::getPlayerUUID).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
