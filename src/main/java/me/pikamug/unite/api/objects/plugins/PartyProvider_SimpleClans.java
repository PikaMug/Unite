package me.pikamug.unite.api.objects.plugins;

import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.events.simpleclans.PartyCreateEvent_SimpleClans;
import me.pikamug.unite.api.events.simpleclans.PartyDeleteEvent_SimpleClans;
import me.pikamug.unite.api.events.simpleclans.PartyJoinEvent_SimpleClans;
import me.pikamug.unite.api.events.simpleclans.PartyLeaveEvent_SimpleClans;
import me.pikamug.unite.api.objects.PartyProvider;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.events.DisbandClanEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.PlayerJoinedClanEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.PlayerKickedClanEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.PreCreateClanEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PartyProvider_SimpleClans extends PartyProvider {
    private SimpleClans simpleClans;
    private final String pluginName = "SimpleClans";

    public PartyProvider_SimpleClans(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new PartyProvider_SimpleClans.PartyServerListener(this), plugin);

        if (simpleClans == null) {
            simpleClans = SimpleClans.getInstance();
        }
    }

    public class PartyServerListener implements Listener {
        final PartyProvider_SimpleClans partyPlugin;

        public PartyServerListener(PartyProvider_SimpleClans partyPlugin) {
            this.partyPlugin = partyPlugin;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            if (partyPlugin.simpleClans == null) {
                if (event.getPlugin().getDescription().getName().equals(pluginName)) {
                    partyPlugin.simpleClans = SimpleClans.getInstance();
                    log.info(String.format("[%s][Party] %s hooked.", plugin.getDescription().getName(), partyPlugin.pluginName));
                }
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event) {
            if (partyPlugin.simpleClans != null) {
                if (event.getPlugin().getDescription().getName().equals(pluginName)) {
                    partyPlugin.simpleClans = null;
                    log.info(String.format("[%s][Party] %s un-hooked.", plugin.getDescription().getName(), partyPlugin.pluginName));
                }
            }
        }

        @EventHandler
        public void onPartyCreate(final PreCreateClanEvent pluginEvent) {
            final PartyCreateEvent event = new PartyCreateEvent_SimpleClans(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyDelete(final DisbandClanEvent pluginEvent) {
            final PartyDeleteEvent event = new PartyDeleteEvent_SimpleClans(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyJoin(final PlayerJoinedClanEvent pluginEvent) {
            final PartyJoinEvent event = new PartyJoinEvent_SimpleClans(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyLeave(final PlayerKickedClanEvent pluginEvent) {
            final PartyLeaveEvent event = new PartyLeaveEvent_SimpleClans(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }
    }

    @Override
    public boolean isPluginEnabled() {
        if (simpleClans == null) {
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
        final Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            return false;
        }
        simpleClans.getClanManager().createClan(player, "&f", partyName);
        return true;
    }

    @Override
    public boolean isPlayerInParty(UUID playerId) {
        return simpleClans.getClanManager().getClanByPlayerUniqueId(playerId) != null;
    }

    @Override
    public boolean areInSameParty(UUID playerId1, UUID playerId2) {
        final Clan one = simpleClans.getClanManager().getClanByPlayerUniqueId(playerId1);
        final Clan two = simpleClans.getClanManager().getClanByPlayerUniqueId(playerId2);
        if (one != null && two != null) {
            final String name1 = one.getName();
            final String name2 = two.getName();
            if (name1 != null && name2 != null) {
                return name1.equals(name2);
            }
        }
        return false;
    }

    @Override
    public String getPartyName(UUID playerId) {
        return Objects.requireNonNull(simpleClans.getClanManager().getClanByPlayerUniqueId(playerId)).getName();
    }

    @Override
    public String getPartyId(UUID playerId) {
        // Return tag since SimpleClans does not use party IDs
        return Objects.requireNonNull(simpleClans.getClanManager().getClanByPlayerUniqueId(playerId)).getTag();
    }

    @Override
    public UUID getLeader(String partyId) {
        final ClanPlayer leader = simpleClans.getClanManager().getClan(partyId).getLeaders().get(0);
        return leader.getUniqueId();
    }

    @Override
    public Set<UUID> getMembers(String partyId) {
        return simpleClans.getClanManager().getClan(partyId).getMembers().stream().map(ClanPlayer::getUniqueId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UUID> getOnlineMembers(String partyId) {
        return simpleClans.getClanManager().getClan(partyId).getOnlineMembers().stream().map(ClanPlayer::getUniqueId)
                .collect(Collectors.toSet());
    }
}
