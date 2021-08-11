package me.pikamug.unite.api.events.dungeonsxl;

import de.erethon.dungeonsxl.api.event.group.GroupPlayerLeaveEvent;
import me.pikamug.unite.api.events.PartyLeaveEvent;
import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyLeaveEvent_DungeonsXL extends PartyLeaveEvent {
    private final PartyProvider partyProvider;
    private final GroupPlayerLeaveEvent event;

    public PartyLeaveEvent_DungeonsXL(PartyProvider partyProvider, Event event, boolean async) {
        super(async);
        this.partyProvider = partyProvider;
        this.event = (GroupPlayerLeaveEvent) event;
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
        return event.getPlayer().getPlayer().getUniqueId();
    }
}
