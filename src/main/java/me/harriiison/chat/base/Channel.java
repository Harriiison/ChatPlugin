package me.harriiison.chat.base;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {

    private String name;
    private String alias;
    private String format;
    private String permission;
    private List<UUID> focusedPlayers = new ArrayList<>();
    private List<UUID> activePlayers = new ArrayList<>();

    public Channel(String name, String alias, String format, String permission) {
        this.name = name;
        this.alias = alias;
        this.format = format;
        this.permission = permission;
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

    public List<UUID> getFocusedPlayers() {
        return focusedPlayers;
    }

    public void addFocusedPlayer(UUID player) {
        focusedPlayers.add(player);
        if (!activePlayers.contains(player))
            addActivePlayer(player);
    }

    public void removeFocusedPlayer(UUID player) {
        focusedPlayers.remove(player);
    }

    public void emptyChannel() {
        activePlayers.clear();
        focusedPlayers.clear();
    }
}
