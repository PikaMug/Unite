package me.pikamug.unite.api.events.parties;

import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostJoinEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyJoinEvent_Parties extends PartyJoinEvent {
    private final PartyProvider partyProvider;
    private final BukkitPartiesPlayerPostJoinEvent event;

    public PartyJoinEvent_Parties(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (BukkitPartiesPlayerPostJoinEvent) event;
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
    public UUID getPlayer() {
        return event.getPartyPlayer().getPlayerUUID();
    }
}
