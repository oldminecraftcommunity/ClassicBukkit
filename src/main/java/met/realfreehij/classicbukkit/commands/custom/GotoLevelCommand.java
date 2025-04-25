package met.realfreehij.classicbukkit.commands.custom;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.server.PlayerInstance;

import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.commands.CommandIssuer;
import met.realfreehij.classicbukkit.utils.ChatColor;

public class GotoLevelCommand extends Command {
    public GotoLevelCommand() {
        super("gotolevel", "Teleport to some level", new String[] {}, true);
    }

    @Override
    public boolean onExecution(String[] args, CommandIssuer issuer) {
    	if(!(issuer instanceof PlayerInstance)) {
    		issuer.sendChatMessage(ChatColor.RED + "You must be a player to execute this command!");
    		return false;
    	}
    	PlayerInstance player = (PlayerInstance) issuer;
    	if(args.length < 1) {
    		player.sendChatMessage(ChatColor.RED + "Usage: /gotolevel <levelname>");
    		return false;
    	}
    	Level level = ClassicBukkit.getServer().getOrLoadOrGenerate(args[0]);
        player.setLevel(level);
        return true;
    }
}