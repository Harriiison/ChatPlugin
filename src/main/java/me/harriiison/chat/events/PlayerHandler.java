package me.harriiison.chat.events;

import me.harriiison.chat.ChatPlugin;
import me.harriiison.chat.base.Channel;
import me.harriiison.chat.base.ChannelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerHandler implements Listener {

    private ChatPlugin plugin;
    private ChannelManager channelManager;
    public PlayerHandler(ChatPlugin instance) {
        this.plugin = instance;
        this.channelManager = plugin.getChannelManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Channel channel : channelManager.channels) {
            if (player.hasPermission(channel.getPermission())) {
                channelManager.joinChannel(player, channel);
            }
        }
        channelManager.getChannelByName("Global").addFocusedPlayer(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        // remove player from all channels
        UUID playerUUID = event.getPlayer().getUniqueId();
        channelManager.getActivePlayerChannels(playerUUID).forEach(ch -> ch.removeActivePlayer(playerUUID));
        channelManager.getFocussedChannel(playerUUID).removeFocusedPlayer(playerUUID);
    }

}
