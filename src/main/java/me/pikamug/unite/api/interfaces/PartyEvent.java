package me.pikamug.unite.api.interfaces;

import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public interface PartyEvent {

    @NotNull
    Event getPluginEvent();

    @NotNull
    PartyProvider getPartyProvider();
}
