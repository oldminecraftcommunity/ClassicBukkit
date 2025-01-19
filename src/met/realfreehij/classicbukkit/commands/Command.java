package met.realfreehij.classicbukkit.commands;

import com.mojang.minecraft.server.PlayerInstance;

public abstract class Command {
    public String name;
    public String description;
    public String[] aliases;
    public final boolean op;

    public Command(String name, String description, String[] aliases, boolean op) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.op = op;
    }
    
    @Deprecated
    public boolean onExecution(String[] args, PlayerInstance player) {
        return false;
    }
    
    public boolean onExecution(String[] args, CommandIssuer issuer) {
    	if(issuer instanceof PlayerInstance) {
    		return this.onExecution(args, (PlayerInstance) issuer);
    	}
    	issuer.sendChatMessage("You must be a player to execute this command.");
    	return false;
    }
}
