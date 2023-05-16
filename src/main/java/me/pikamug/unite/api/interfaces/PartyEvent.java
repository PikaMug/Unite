package me.pikamug.unite.api.interfaces;

import me.pikamug.unite.api.objects.PartyProvider;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PartyEvent {

    @Nullable
    Event getPluginEvent();

    @NotNull
    PartyProvider getPartyProvider();
}
