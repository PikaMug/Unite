package me.pikamug.unite.api.objects.plugins;

import com.booksaw.betterTeams.Main;
import com.booksaw.betterTeams.PlayerRank;
import com.booksaw.betterTeams.Team;
import com.booksaw.betterTeams.customEvents.CreateTeamEvent;
import com.booksaw.betterTeams.customEvents.DisbandTeamEvent;
import com.booksaw.betterTeams.customEvents.PlayerJoinTeamEvent;
import com.booksaw.betterTeams.customEvents.PlayerLeaveTeamEvent;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.events.betterteams.PartyCreateEvent_BetterTeams;
import me.pikamug.unite.api.events.betterteams.PartyDeleteEvent_BetterTeams;
import me.pikamug.unite.api.events.betterteams.PartyJoinEvent_BetterTeams;
import me.pikamug.unite.api.events.betterteams.PartyLeaveEvent_BetterTeams;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

public class PartyProvider_BetterTeams extends PartyProvider {
    private Main betterTeams;
    private final String pluginName = "BetterTeams";

    public PartyProvider_BetterTeams(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new PartyServerListener(this), plugin);

        if (betterTeams == null) {
            betterTeams = (Main) Bukkit.getPluginManager().getPlugin(pluginName);
        }
    }

    public class PartyServerListener implements Listener {
        final PartyProvider_BetterTeams partyPlugin;

        public PartyServerListener(PartyProvider_BetterTeams partyPlugin) {
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
        public void onPartyCreate(final CreateTeamEvent pluginEvent) {
            final PartyCreateEvent event = new PartyCreateEvent_BetterTeams(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyDelete(final DisbandTeamEvent pluginEvent) {
            final PartyDeleteEvent event = new PartyDeleteEvent_BetterTeams(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyJoin(final PlayerJoinTeamEvent pluginEvent) {
            final PartyJoinEvent event = new PartyJoinEvent_BetterTeams(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyLeave(final PlayerLeaveTeamEvent pluginEvent) {
            final PartyLeaveEvent event = new PartyLeaveEvent_BetterTeams(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }
    }

    @Override
    public boolean isPluginEnabled() {
        if (betterTeams == null) {
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
        return Team.getTeamManager().createNewTeam(partyName, plugin.getServer().getPlayer(playerId)) != null;
    }

    @Override
    public boolean isPlayerInParty(UUID playerId) {
        return Team.getTeamManager().isInTeam(plugin.getServer().getOfflinePlayer(playerId));
    }

    @Override
    public boolean areInSameParty(UUID playerId1, UUID playerId2) {
        final Team one = Team.getTeam(plugin.getServer().getOfflinePlayer(playerId1));
        final Team two = Team.getTeam(plugin.getServer().getOfflinePlayer(playerId2));
        return one != null && two != null && one.getID().equals(two.getID());
    }

    @Override
    public String getPartyName(UUID playerId) {
        return Team.getTeam(plugin.getServer().getOfflinePlayer(playerId)).getName();
    }

    @Override
    public String getPartyId(UUID playerId) {
        return Team.getTeam(plugin.getServer().getOfflinePlayer(playerId)).getID().toString();
    }

    @Override
    public UUID getLeader(String partyId) {
        final Team team = Team.getTeam(UUID.fromString(partyId));
        if (team != null) {
            return team.getMembers().getRank(PlayerRank.OWNER).get(0).getPlayer().getUniqueId();
        }
        return null;
    }

    @Override
    public Set<UUID> getMembers(String partyId) {
        return Team.getTeam(UUID.fromString(partyId)).getMembers().getOfflinePlayers().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toSet());
    }

    @Override
    public Set<UUID> getOnlineMembers(String partyId) {
        return Team.getTeam(UUID.fromString(partyId)).getOnlineMembers().stream().map(Player::getUniqueId).collect(Collectors.toSet());
    }
}
