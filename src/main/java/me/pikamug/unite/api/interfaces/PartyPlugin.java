package me.pikamug.unite.api.interfaces;

import java.util.Set;
import java.util.UUID;

public interface PartyPlugin {
    boolean isPluginEnabled();

    String getPluginName();

    boolean createParty(String partyName, UUID playerId);

    boolean isPlayerInParty(UUID playerId);

    boolean areInSameParty(UUID playerId1, UUID playerId2);

    String getPartyName(UUID playerId);

    String getPartyId(UUID playerId);

    UUID getLeader(String partyId);

    Set<UUID> getMembers(String partyId);

    Set<UUID> getOnlineMembers(String partyId);
}
