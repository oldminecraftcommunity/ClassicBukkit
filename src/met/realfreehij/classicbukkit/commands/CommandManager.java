package met.realfreehij.classicbukkit.commands;

import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.custom.*;
import met.realfreehij.classicbukkit.commands.vanilla.*;
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
        commands.add(new StopCommand());
        commands.add(new OpCommand());
        commands.add(new DeopCommand());
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void executeCommand(String cmd, String[] args, CommandIssuer issuer) {
        for(Command command : commands) {
            if(command.name.equalsIgnoreCase(cmd) || Arrays.asList(command.aliases).contains(cmd)) {
                if(command.op) {
                    if(ClassicBukkit.getServer().isAdmin(issuer)) {
                        if(command.onExecution(args, issuer)) {
                            issuer.sendChatMessage(ChatColor.RED + "You dont have permission to execute this command!");
                        }
                    } else {
                        issuer.sendChatMessage(ChatColor.RED + "You dont have permission to execute this command!");
                    }
                    return;
                } else {
                    command.onExecution(args, issuer);
                    return;
                }
            }
        }
        issuer.sendChatMessage(ChatColor.WHITE + "Unknown command.");
    }
}
