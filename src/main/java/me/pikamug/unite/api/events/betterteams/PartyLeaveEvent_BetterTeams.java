package me.pikamug.unite.api.events.betterteams;

import com.booksaw.betterTeams.customEvents.PlayerLeaveTeamEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyLeaveEvent_BetterTeams extends PartyLeaveEvent {
    private final PartyProvider partyProvider;
    private final PlayerLeaveTeamEvent event;

    public PartyLeaveEvent_BetterTeams(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (PlayerLeaveTeamEvent) event;
    }

    @Override
    public Event getPluginEvent() {
        return event;
    }

    @Override
    public @NotNull PartyProvider getPartyProvider() {
        return partyProvider;
    }

    @Override
    public UUID getPlayer() {
        return event.getPlayer().getUniqueId();
    }
}
