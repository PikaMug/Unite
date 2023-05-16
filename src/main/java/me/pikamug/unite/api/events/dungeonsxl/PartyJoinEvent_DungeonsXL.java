package me.pikamug.unite.api.events.dungeonsxl;

import de.erethon.dungeonsxl.api.event.group.GroupPlayerJoinEvent;
import me.pikamug.unite.api.events.PartyJoinEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyJoinEvent_DungeonsXL extends PartyJoinEvent {
    private final PartyProvider partyProvider;
    private final GroupPlayerJoinEvent event;

    public PartyJoinEvent_DungeonsXL(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (GroupPlayerJoinEvent) event;
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
        return event.getPlayer().getPlayer().getUniqueId();
    }
}
