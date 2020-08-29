package me.harriiison.chat.commands;

import me.harriiison.chat.ChatMain;
import me.harriiison.chat.base.Channel;
import me.harriiison.chat.base.ChannelManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChannelCommand implements CommandExecutor {
    private ChatMain plugin;
    private ChannelManager channelManager;
    public ChannelCommand(ChatMain instance) {
        this.plugin = instance;
        this.channelManager = instance.getChannelManager();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("channel") || command.getName().equalsIgnoreCase("ch")) {

            if (sender instanceof ConsoleCommandSender) {
                if (args.length == 0) {
                    return sendHelpMessage(sender);
                }
                if (args[0].equals("join") || args[0].equals("leave")) {
                    sender.sendMessage(ChatColor.RED + "The console cannot join or leave channels!");
                    return true;
                }
                else if (args[0].equals("list")) {
                    sender.sendMessage(ChatColor.GOLD + "Channel List");
                    channelManager.channels.forEach(ch -> {
                        sender.sendMessage(ch.getName() + " (" + ch.getAlias() + ") : " + ch.getActivePlayers().size() + " active users");
                    });
                    return true;
                }
                else {
                    return sendHelpMessage(sender);
                }
            }
            else if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You cannot execute channel commands!");
                return true;
            }
            Player player = (Player) sender;

            // send command help
            if (args.length == 0) {
                return sendHelpMessage(player);
            }
            if (args[0].equals("help")) {
                return sendHelpMessage(player);
            }
            else if (args[0].equals("list")) {
                List<Channel> activeChannels = channelManager.getActivePlayerChannels(player.getUniqueId());

                player.sendMessage(ChatColor.GOLD + "Channel List");
                channelManager.channels.forEach(ch -> {
                    if (player.hasPermission(ch.getPermission())) {
                        player.sendMessage(ch.getName() + " (" + ch.getAlias() + ")" + (activeChannels.contains(ch) ? " : Joined" : ""));
                    }
                });

                return true;
            }

            Channel channel = channelManager.getChannelByName(args.length == 1 ? args[0] : args[1]);
            if (channel == null) {
                player.sendMessage(ChatColor.RED + "The specified channel does not exist!");
                return sendHelpMessage(player);
            }

            if (args[0].equals("join")) {
                if (player.hasPermission(channel.getPermission()))
                    channelManager.joinChannel(player, channel);
                else
                    player.sendMessage(ChatColor.RED + "You cannot join this channel!");
                return true;
            }
            else if (args[0].equals("leave")) {
                channelManager.leaveChannel(player, channel);

                if (channelManager.getActivePlayerChannels(player.getUniqueId()).size() == 0) {
                    player.sendMessage(ChatColor.RED + "You cannot leave every channel!");
                    channelManager.setFocussedChannel(player, "Global");
                    return true;
                }

                return true;
            } else {
                // player did /ch [channel name/alias] to focus
                if (channel == null) {
                    player.sendMessage(ChatColor.RED + "The specified channel does not exist!");
                    return sendHelpMessage(player);
                }
               if (player.hasPermission(channel.getPermission()))
                   channelManager.setFocussedChannel(player, channel.getName());
               else
                   player.sendMessage(ChatColor.RED + "You cannot focus on this channel!");
                return true;
            }
        }
        return true;
    }

    private boolean sendHelpMessage(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "Chat Commands:");
        sender.sendMessage(ChatColor.GOLD + "/ch help" + ChatColor.DARK_GRAY + " >> " + ChatColor.YELLOW + "Display this menu");
        sender.sendMessage(ChatColor.GOLD + "/ch list" + ChatColor.DARK_GRAY + " >> " + ChatColor.YELLOW + "Display up the list of channels");
        sender.sendMessage(ChatColor.GOLD + "/ch join [channel name/alias]" + ChatColor.DARK_GRAY + " >> " + ChatColor.YELLOW + "Join the specified channel");
        sender.sendMessage(ChatColor.GOLD + "/ch leave [channel name/alias]" + ChatColor.DARK_GRAY + " >> " + ChatColor.YELLOW + "Leave the specified channel");
        sender.sendMessage(ChatColor.GOLD + "/ch [channel name/alias]" + ChatColor.DARK_GRAY + " >> " + ChatColor.YELLOW + "Focus on the specified channel");
        sender.sendMessage("");
        return true;
    }
}
