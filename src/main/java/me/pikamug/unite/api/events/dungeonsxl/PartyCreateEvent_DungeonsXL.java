package me.pikamug.unite.api.events.dungeonsxl;

import de.erethon.dungeonsxl.api.event.group.GroupCreateEvent;
import me.pikamug.unite.api.events.PartyCreateEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PartyCreateEvent_DungeonsXL extends PartyCreateEvent {
    private final PartyProvider partyProvider;
    private final GroupCreateEvent event;

    public PartyCreateEvent_DungeonsXL(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (GroupCreateEvent) event;
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
    public UUID getCreator() {
        return Objects.requireNonNull(event.getCreator()).getUniqueId();
    }
}
