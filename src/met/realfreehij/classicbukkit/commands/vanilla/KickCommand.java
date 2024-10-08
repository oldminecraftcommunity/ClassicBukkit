package met.realfreehij.classicbukkit.commands.vanilla;

import com.mojang.minecraft.server.MinecraftServer;
import com.mojang.minecraft.server.PlayerInstance;
import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.utils.ChatColor;

import java.util.Arrays;
import java.util.List;

public class KickCommand extends Command {
    public KickCommand() {
        super("kick", "Allows operators to kick players", new String[] {}, true);
    }

    @Override
    public boolean onExecution(String[] args, PlayerInstance player) {
        if(args.length > 0) {
        	MinecraftServer serv = ClassicBukkit.getServer();
        	List<PlayerInstance> players = serv.getPlayerList();
        	for(int i = 0; i < players.size(); ++i) {
        		PlayerInstance player2kick = players.get(i);
        		if(player2kick.name.equalsIgnoreCase(args[0])) {
        			 String reason = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "no reason";
        			 serv.getPlayerByName(args[0]).kick(reason);
        			 
                     for(PlayerInstance player1 : players) {
                         player1.sendChatMessage(ChatColor.WHITE + args[0] + " was kicked for " + reason);
                     }
                     return false;
        		}
        	}
        	player.sendChatMessage(ChatColor.RED + "Player " + args[0] + " is not online");
        } else {
            player.sendChatMessage(ChatColor.RED + "Usage: /kick <player> <reason>");
        }
        return false;
    }
}
