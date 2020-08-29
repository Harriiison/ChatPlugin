package me.harriiison.chat;

import me.harriiison.chat.base.Channel;
import me.harriiison.chat.base.ChannelManager;
import me.harriiison.chat.commands.ChannelCommand;
import me.harriiison.chat.commands.ChatCommand;
import me.harriiison.chat.events.ChatMessage;
import me.harriiison.chat.events.PlayerHandler;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatMain extends JavaPlugin {

    private ChannelManager channelManager;

    private Chat chat = null;

    @Override
    public void onEnable() {
        setupChat();

        channelManager = new ChannelManager();

        // TODO initiate channels in a loop from config
        Channel globalChannel = new Channel("Global", "g", "&2[G] &7{DISPLAY_NAME}&2: &f", "chat.global");
        Channel staffChannel = new Channel("Staff", "sc", "&4&l[Staff] &6{DISPLAY_NAME}&4: &7", "chat.staff");
        Channel adminChannel = new Channel("Admin", "a", "&3&l[Admin] &6{DISPLAY_NAME}&3: &7", "chat.admin");
        channelManager.setupChannel(globalChannel);
        channelManager.setupChannel(staffChannel);
        channelManager.setupChannel(adminChannel);

        getServer().getPluginManager().registerEvents(new ChatMessage(this), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(this), this);

        getCommand("channel").setExecutor(new ChannelCommand(this));

        channelManager.listChannels().forEach(channel -> getCommand(channel.getName()).setExecutor(new ChatCommand(this)));

        // Add all online players to Global on reload
        Bukkit.getOnlinePlayers().forEach(player -> {
            channelManager.getChannelByName("Global").addFocussedPlayer(player.getUniqueId());
        });

    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }
}
