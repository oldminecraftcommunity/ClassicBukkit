package met.realfreehij.classicbukkit.commands.custom;

import com.mojang.minecraft.server.PlayerInstance;
import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.commands.CommandIssuer;
import met.realfreehij.classicbukkit.utils.ChatColor;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Show the list of all commands", new String[] {"?"}, false);
    }

    @Override
    public boolean onExecution(String[] args, CommandIssuer player) {
    	player.sendChatMessage("&00&11&22&33&44&55&66&77&88&99&aa&bb&cc&dd&ee&ff");
        player.sendChatMessage(ChatColor.WHITE + " - " + ChatColor.GREEN + "Showing list of commands" + ChatColor.WHITE + " - ");
        for(Command command : ClassicBukkit.commandManager.getCommands()) {
            if(command.op) {
                if(ClassicBukkit.getServer().isAdmin(player)) {
                    player.sendChatMessage(ChatColor.YELLOW + "/" + command.name + ChatColor.WHITE + ": " + command.description);
                }
            } else {
                player.sendChatMessage(ChatColor.YELLOW + "/" + command.name + ChatColor.WHITE + ": " + command.description);
            }
        }
        return false;
    }
}
