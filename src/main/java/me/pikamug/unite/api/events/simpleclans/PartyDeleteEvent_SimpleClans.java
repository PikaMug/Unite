package me.pikamug.unite.api.events.simpleclans;

import me.pikamug.unite.api.events.PartyDeleteEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import net.sacredlabyrinth.phaed.simpleclans.events.DisbandClanEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyDeleteEvent_SimpleClans extends PartyDeleteEvent {
    private final PartyProvider partyProvider;
    private final DisbandClanEvent event;

    public PartyDeleteEvent_SimpleClans(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (DisbandClanEvent) event;
    }

    @Override
    public @NotNull Event getPluginEvent() {
        return event;
    }

    @Override
    public @NotNull PartyProvider getPartyProvider() {
        return partyProvider;
    }

    @Override
    public UUID getDisbander() {
        final CommandSender cs = event.getSender();
        if (cs instanceof OfflinePlayer) {
            return ((OfflinePlayer)cs).getUniqueId();
        }
        return null;
    }
}
