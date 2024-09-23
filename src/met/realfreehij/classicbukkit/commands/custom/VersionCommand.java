package met.realfreehij.classicbukkit.commands.custom;

import com.mojang.minecraft.net.Packet;
import com.mojang.minecraft.server.PlayerInstance;
import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.utils.ChatColor;

import java.awt.*;

public class VersionCommand extends Command {
    public VersionCommand() {
        super("version", "Get the current server core version", new String[] {"ver"}, false);
    }

    @Override
    public boolean onExecution(String[] args, PlayerInstance player) {
        player.sendChatMessage(ChatColor.WHITE + "This server is running " + ChatColor.GREEN + ClassicBukkit.name);
        player.sendChatMessage(ChatColor.WHITE + "Version: " + ClassicBukkit.version);
        return false;
    }
}
