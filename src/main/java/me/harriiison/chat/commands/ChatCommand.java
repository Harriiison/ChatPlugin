package me.harriiison.chat.commands;

import me.harriiison.chat.ChatMain;
import me.harriiison.chat.base.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ChatCommand implements CommandExecutor {
    private ChatMain plugin;
    public ChatCommand(ChatMain instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Channel channel = plugin.getChannelManager().getChannelByName(command.getName());

        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "You cannot send an empty message!");
                return true;
            }

            String formattedMessage = ChatColor.translateAlternateColorCodes('&', channel.getFormat().replace("{DISPLAY_NAME}", "&4[Console]") + String.join(" ", args));

            channel.getActivePlayers().forEach(playerUUID -> Bukkit.getPlayer(playerUUID).sendMessage(formattedMessage));
            Bukkit.getConsoleSender().sendMessage(formattedMessage);
            return true;
        }
        else if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You cannot send messages in this channel!");
            return true;
        }
        Player player = (Player) sender;

        if (channel == null)
            return true;

        if (!(player.hasPermission(channel.getPermission())))
            return true;

        if (!(channel.getActivePlayers().contains(player.getUniqueId()))) {
            player.sendMessage(ChatColor.RED + "You cannot send messages in a channel you have not joined!");
            player.sendMessage(ChatColor.RED + "To join this channel do " + ChatColor.GOLD + "/ch join " + channel.getAlias());
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "You cannot send an empty message!");
            return true;
        }

        String formattedMessage = ChatColor.translateAlternateColorCodes('&', channel.getFormat().replace("{DISPLAY_NAME}", player.getDisplayName()) + String.join(" ", args));

        channel.getActivePlayers().forEach(playerUUID -> Bukkit.getPlayer(playerUUID).sendMessage(formattedMessage));
        Bukkit.getConsoleSender().sendMessage(formattedMessage);

        return true;
    }
}
