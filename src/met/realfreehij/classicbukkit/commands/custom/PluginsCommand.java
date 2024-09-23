package met.realfreehij.classicbukkit.commands.custom;

import com.mojang.minecraft.server.PlayerInstance;
import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.plugins.PluginEntry;
import met.realfreehij.classicbukkit.utils.ChatColor;

public class PluginsCommand extends Command {
    public PluginsCommand() {
        super("plugins", "See the list of server plugins", new String[] {"pl"}, false);
    }

    @Override
    public boolean onExecution(String[] args, PlayerInstance player) {
        player.sendChatMessage(ChatColor.WHITE + " - " + ChatColor.GREEN + "Showing list of plugins" + ChatColor.WHITE + " - ");
        for(PluginEntry pluginEntry : ClassicBukkit.pluginManager.getLoadedPlugins()) {
            player.sendChatMessage(ChatColor.WHITE + pluginEntry.getName() + ChatColor.GREEN + " v" + pluginEntry.getVersion() + ChatColor.WHITE + " by " + ChatColor.YELLOW + pluginEntry.getAuthor());
        }
        return false;
    }
}
