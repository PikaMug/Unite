package me.pikamug.unite.api.objects.plugins;

import de.erethon.dungeonsxl.DungeonsXL;
import de.erethon.dungeonsxl.api.event.group.GroupCreateEvent;
import de.erethon.dungeonsxl.api.event.group.GroupDisbandEvent;
import de.erethon.dungeonsxl.api.event.group.GroupPlayerJoinEvent;
import de.erethon.dungeonsxl.api.event.group.GroupPlayerLeaveEvent;
import de.erethon.dungeonsxl.api.player.PlayerGroup;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.events.dungeonsxl.PartyCreateEvent_DungeonsXL;
import me.pikamug.unite.api.events.dungeonsxl.PartyDeleteEvent_DungeonsXL;
import me.pikamug.unite.api.events.dungeonsxl.PartyJoinEvent_DungeonsXL;
import me.pikamug.unite.api.events.dungeonsxl.PartyLeaveEvent_DungeonsXL;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PartyProvider_DungeonsXL extends PartyProvider {
    private DungeonsXL dungeonsXl;
    private final String pluginName = "DungeonsXL";

    public PartyProvider_DungeonsXL(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new PartyServerListener(this), plugin);

        if (dungeonsXl == null) {
            dungeonsXl = DungeonsXL.getInstance();
        }
    }

    public class PartyServerListener implements Listener {
        final PartyProvider_DungeonsXL partyPlugin;

        public PartyServerListener(PartyProvider_DungeonsXL partyPlugin) {
            this.partyPlugin = partyPlugin;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            if (partyPlugin.dungeonsXl == null) {
                if (event.getPlugin().getDescription().getName().equals(pluginName)) {
                    partyPlugin.dungeonsXl = DungeonsXL.getInstance();
                    log.info(String.format("[%s][Party] %s hooked.", plugin.getDescription().getName(), partyPlugin.pluginName));
                }
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event) {
            if (partyPlugin.dungeonsXl != null) {
                if (event.getPlugin().getDescription().getName().equals(pluginName)) {
                    partyPlugin.dungeonsXl = null;
                    log.info(String.format("[%s][Party] %s un-hooked.", plugin.getDescription().getName(), partyPlugin.pluginName));
                }
            }
        }

        @EventHandler
        public void onPartyCreate(final GroupCreateEvent pluginEvent) {
            final PartyCreateEvent event = new PartyCreateEvent_DungeonsXL(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyDelete(final GroupDisbandEvent pluginEvent) {
            final PartyDeleteEvent event = new PartyDeleteEvent_DungeonsXL(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyJoin(final GroupPlayerJoinEvent pluginEvent) {
            final PartyJoinEvent event = new PartyJoinEvent_DungeonsXL(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }

        @EventHandler
        public void onPartyLeave(final GroupPlayerLeaveEvent pluginEvent) {
            final PartyLeaveEvent event = new PartyLeaveEvent_DungeonsXL(partyPlugin, pluginEvent, pluginEvent.isAsynchronous());
            plugin.getServer().getPluginManager().callEvent(event);
        }
    }

    @Override
    public boolean isPluginEnabled() {
        if (dungeonsXl == null) {
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
        return dungeonsXl.createGroup(plugin.getServer().getPlayer(playerId)) != null;
    }

    @Override
    public boolean isPlayerInParty(UUID playerId) {
        return dungeonsXl.getPlayerGroup(plugin.getServer().getPlayer(playerId)) != null;
    }

    @Override
    public boolean areInSameParty(UUID playerId1, UUID playerId2) {
        final PlayerGroup one = dungeonsXl.getPlayerGroup(plugin.getServer().getPlayer(playerId1));
        final PlayerGroup two = dungeonsXl.getPlayerGroup(plugin.getServer().getPlayer(playerId2));
        return one.getId() == two.getId();
    }

    @Override
    public String getPartyName(UUID playerId) {
        return dungeonsXl.getPlayerGroup(plugin.getServer().getPlayer(playerId)).getName();
    }

    @Override
    public String getPartyId(UUID playerId) {
        return String.valueOf(dungeonsXl.getPlayerGroup(plugin.getServer().getPlayer(playerId)).getId());
    }

    @Override
    public UUID getLeader(String partyId) {
        final int pid = Integer.parseInt(partyId);
        if (getGroupById(pid) != null) {
            return getGroupById(pid).getLeader().getUniqueId();
        }
        return null;
    }

    @Override
    public Set<UUID> getMembers(String partyId) {
        final int pid = Integer.parseInt(partyId);
        if (getGroupById(pid) != null) {
            return new HashSet<>(getGroupById(pid).getMembers().getUniqueIds());
        }
        return Collections.emptySet();
    }

    @Override
    public Set<UUID> getOnlineMembers(String partyId) {
        final int pid = Integer.parseInt(partyId);
        if (getGroupById(pid) != null) {
            return getGroupById(pid).getMembers().getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public PlayerGroup getGroupById(int id) {
        for (PlayerGroup group : dungeonsXl.getGroupCache()) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }
}
