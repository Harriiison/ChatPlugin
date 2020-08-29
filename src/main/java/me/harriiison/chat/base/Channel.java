package me.harriiison.chat.base;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {

    private String name;
    private String alias;
    private String format;
    private String permission;
    private List<UUID> focussedPlayers = new ArrayList<>();
    private List<UUID> activePlayers = new ArrayList<>();

    public Channel(String channelName, String channelAlias, String channelFormat, String channelPermission) {
        this.name = channelName;
        this.alias = channelAlias;
        this.format = channelFormat;
        this.permission = channelPermission;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getFormat() {
        return format;
    }

    public String getPermission() {
        return permission;
    }

    public List<UUID> getActivePlayers() {
        return activePlayers;
    }

    public void addActivePlayer(UUID player) {
        activePlayers.add(player);
    }

    public void removeActivePlayer(UUID player) {
        activePlayers.remove(player);
    }

    public List<UUID> getFocussedPlayers() {
        return focussedPlayers;
    }

    public void addFocussedPlayer(UUID player) {
        focussedPlayers.add(player);
        if (!activePlayers.contains(player))
            addActivePlayer(player);
    }

    public void removeFocussedPlayer(UUID player) {
        focussedPlayers.remove(player);
    }

    public void emptyChannel() {
        activePlayers.clear();
        focussedPlayers.clear();
    }
}
