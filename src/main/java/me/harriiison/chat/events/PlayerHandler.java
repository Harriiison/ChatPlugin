package me.harriiison.chat.events;

import me.harriiison.chat.ChatMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerHandler implements Listener {

    private ChatMain plugin;
    public PlayerHandler(ChatMain instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getChannelManager().getChannelByName("Global").addFocussedPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        // remove player from all channels
        UUID playerUUID = event.getPlayer().getUniqueId();
        plugin.getChannelManager().getActivePlayerChannels(playerUUID).forEach(ch -> ch.removeActivePlayer(playerUUID));
        plugin.getChannelManager().getFocussedChannel(playerUUID).removeFocussedPlayer(playerUUID);
    }

}
