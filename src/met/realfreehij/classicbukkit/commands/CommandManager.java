package met.realfreehij.classicbukkit.commands;

import com.mojang.minecraft.server.PlayerInstance;
import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.custom.HelpCommand;
import met.realfreehij.classicbukkit.commands.custom.PluginsCommand;
import met.realfreehij.classicbukkit.commands.custom.SaveCommand;
import met.realfreehij.classicbukkit.commands.custom.VersionCommand;
import met.realfreehij.classicbukkit.commands.vanilla.BanCommand;
import met.realfreehij.classicbukkit.commands.vanilla.KickCommand;
import met.realfreehij.classicbukkit.utils.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {
    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        commands.add(new KickCommand());
        commands.add(new HelpCommand());
        commands.add(new VersionCommand());
        commands.add(new PluginsCommand());
        commands.add(new SaveCommand());
        commands.add(new BanCommand());
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void executeCommand(String cmd, String[] args, PlayerInstance player) {
        for(Command command : commands) {
            if(command.name.equalsIgnoreCase(cmd) || Arrays.asList(command.aliases).contains(cmd)) {
                if(command.op) {
                    if(ClassicBukkit.getServer().admins.containsPlayer(player.name)) {
                        if(command.onExecution(args, player)) {
                            player.sendChatMessage(ChatColor.RED + "You dont have permission to execute this command!");
                        }
                    } else {
                        player.sendChatMessage(ChatColor.RED + "You dont have permission to execute this command!");
                    }
                    return;
                } else {
                    command.onExecution(args, player);
                    return;
                }
            }
        }
        player.sendChatMessage(ChatColor.WHITE + "Unknown command.");
    }
}
