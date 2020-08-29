package me.harriiison.chat.events;

import me.harriiison.chat.ChatMain;
import me.harriiison.chat.base.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatMessage implements Listener {

    private ChatMain plugin;
    public ChatMessage(ChatMain instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // This is when a player just types into their chat with no channel specified
        Player player = event.getPlayer();
        event.getRecipients().clear();

        Channel focussedChannel = plugin.getChannelManager().getFocussedChannel(player.getUniqueId());

        if (!(player.hasPermission(focussedChannel.getPermission()))) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You do not have permission to send messages in this channel");
            plugin.getChannelManager().setFocussedChannel(player, "Global");
            return;
        }

        focussedChannel.getActivePlayers().forEach(uuid -> event.getRecipients().add(Bukkit.getPlayer(uuid)));

        // e.g. "[G] " + "Hello Everyone!"
        event.setFormat(
                ChatColor.translateAlternateColorCodes('&', focussedChannel.getFormat().replace("{DISPLAY_NAME}", player.getDisplayName()) + event.getMessage())
        );
    }
}
