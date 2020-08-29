package me.harriiison.chat.base;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelManager {

    public List<Channel> channels = new ArrayList<>();

    public void setupChannel(Channel channel) {
        channels.add(channel);
    }

    public List<Channel> listChannels() {
        return channels;
    }

    public Channel getChannelByName(String channelName) {
        for (Channel channel : channels) {
            if (channel.getName().equalsIgnoreCase(channelName)) {
                return channel;
            }
            else if (channel.getAlias().equalsIgnoreCase(channelName)) {
                return channel;
            }
        }
        return null;
    }

    public boolean channelHasPlayer(Channel channel, UUID player) {
        return channel.getActivePlayers().contains(player);
    }

    // Join a channel and see the messages from it in the background
    public void joinChannel(Player player, Channel channel) {
        if (channel == null) {
            player.sendMessage(ChatColor.RED + "The specified channel does not exist!");
            return;
        }
        if (channelHasPlayer(channel, player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are already in the channel " + ChatColor.GOLD + channel.getName());
            player.sendMessage(ChatColor.RED + "Use " + ChatColor.GOLD + "/" + channel.getAlias() + " [message]" + ChatColor.RED + " to send a message");
            return;
        }
        channel.addActivePlayer(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "You joined " + ChatColor.GOLD + channel.getName());
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "Use " + ChatColor.GOLD + "/" + channel.getAlias() + " [message]" + ChatColor.GREEN + " to send a message");
        player.sendMessage(ChatColor.GREEN + "or " + ChatColor.GOLD + "/ch " + channel.getAlias() + ChatColor.GREEN + " to focus on this channel");
    }

    // Leave a channel and no longer see its messages
    public void leaveChannel(Player player, Channel channel) {
        if (channel == null) {
            player.sendMessage(ChatColor.RED + "The specified channel does not exist!");
            return;
        }
        channel.removeActivePlayer(player.getUniqueId());
        player.sendMessage(ChatColor.RED + "You left " + ChatColor.GOLD + channel.getName());

        // TODO if player is in no channels add them back to global
    }

    // Default channel that every message from the player is sent to
    public void setFocussedChannel(Player player, String channelName) {
        Channel channel = getChannelByName(channelName);
        if (channel == null) {
            player.sendMessage(ChatColor.RED + "The channel " + ChatColor.GOLD + channelName + ChatColor.RED + " does not exist!");
            return;
        }
        Channel focussedChannel = getFocussedChannel(player.getUniqueId());
        if (focussedChannel == channel) {
            player.sendMessage(ChatColor.RED + "You are already focussed to the channel " + ChatColor.GOLD + focussedChannel.getName());
            return;
        }
        channel.addFocussedPlayer(player.getUniqueId());

        // remove focussed player from other channel
        if (focussedChannel != null)
            focussedChannel.removeFocussedPlayer(player.getUniqueId());

        player.sendMessage(ChatColor.GREEN + "You are now focussed on the channel " + ChatColor.GOLD + channel.getName());
    }

    public Channel getFocussedChannel(UUID player) {
        for (Channel ch : channels) {
            if (ch.getFocussedPlayers().contains(player)) {
                return ch;
            }
        }
        return getChannelByName("Global");
    }

    public List<Channel> getActivePlayerChannels(UUID player) {
        List<Channel> activePlayerChannels = new ArrayList<>();
        for (Channel ch : channels) {
            if (ch.getActivePlayers().contains(player)) {
                activePlayerChannels.add(ch);
            }
        }
        return activePlayerChannels;
    }
}
