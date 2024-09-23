package met.realfreehij.classicbukkit.commands.vanilla;

import com.mojang.minecraft.server.PlayerInstance;
import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.utils.ChatColor;

import java.util.Arrays;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban", "Disallow the player from entering the server", new String[] {}, true);
    }

    @Override
    public boolean onExecution(String[] args, PlayerInstance player) {
        if(args.length > 0) {
            ClassicBukkit.getServer().banned.addPlayer(args[0]);
            String reason = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "no reason";
            for(Object player1 : ClassicBukkit.getServer().getPlayerList()) {
                ((PlayerInstance)player1).sendChatMessage(ChatColor.WHITE + args[0] + " was banned for " + reason);
            }
        } else {
            player.sendChatMessage(ChatColor.RED + "Usage: /ban <player> <reason>");
        }
        return false;
    }
}
