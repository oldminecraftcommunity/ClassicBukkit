package met.realfreehij.classicbukkit.commands;

import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.custom.*;
import met.realfreehij.classicbukkit.commands.vanilla.*;
import met.realfreehij.classicbukkit.utils.ChatColor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.mojang.minecraft.server.MinecraftServer;

public class CommandManager {
    public final HashMap<String, Command> commands = new HashMap<>();
    public final HashSet<Command> commandsList = new HashSet<>();
    public CommandManager() {
    	addCommand(new KickCommand());
    	addCommand(new HelpCommand());
    	addCommand(new VersionCommand());
    	addCommand(new PluginsCommand());
    	addCommand(new SaveCommand());
    	addCommand(new BanCommand());
    	addCommand(new StopCommand());
    	addCommand(new OpCommand());
    	addCommand(new DeopCommand());
    }

    public void addCommand(Command command) {
    	String cmd = command.name.toLowerCase();
    	Command oldcmd = commands.get(cmd);
    	if(oldcmd != null) {
    		MinecraftServer.logger.warning(String.format("Command with name \"%s\" is already registered(old: %s, new: %s), overriding!", cmd, oldcmd.getClass().getName(), command.getClass().getName()));
    	}
    	commandsList.add(command);
        commands.put(cmd, command);
        for(String alias : command.aliases) {
        	alias = alias.toLowerCase();
        	Command ss = commands.get(alias);
        	if(ss != null) {
        		MinecraftServer.logger.warning(String.format("Command with name \"%s\" is already registered(old: %s, new: %s), overriding!", alias, ss.getClass().getName(), command.getClass().getName()));
        	}
        	commands.put(alias, command);
        }
    }

    public void executeCommand(String cmd, String[] args, CommandIssuer issuer) {
    	Command command = commands.get(cmd.toLowerCase());
    	if(command == null) {
    		issuer.sendChatMessage(ChatColor.WHITE + "Unknown command.");
    		return;
    	}
    	
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
    
    /**
     * @deprecated Use commandsList
     * @return
     */
    @Deprecated
    public ArrayList<Command> getCommands() {
        return new ArrayList<>(commandsList);
    }
}
