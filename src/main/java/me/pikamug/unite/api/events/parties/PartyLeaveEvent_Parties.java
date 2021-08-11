package me.pikamug.unite.api.events.parties;

import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostLeaveEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyLeaveEvent_Parties extends PartyLeaveEvent {
    private final PartyProvider partyProvider;
    private final BukkitPartiesPlayerPostLeaveEvent event;

    public PartyLeaveEvent_Parties(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (BukkitPartiesPlayerPostLeaveEvent) event;
    }

    @Override
    public @NotNull Event getPluginEvent() {
        return event;
    }

    @Override
    public PartyProvider getPartyProvider() {
        return partyProvider;
    }

    @Override
    public UUID getPlayer() {
        return event.getPartyPlayer().getPlayerUUID();
    }
}
