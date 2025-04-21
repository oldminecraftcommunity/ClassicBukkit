package met.realfreehij.classicbukkit.commands.custom;

import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.commands.CommandIssuer;
import met.realfreehij.classicbukkit.utils.ChatColor;
import met.realfreehij.classicbukkit.ClassicBukkit;

public class DeopCommand extends Command {
    public DeopCommand() {
        super("deop", "Remove player from operators", new String[] {}, true);
    }

    @Override
    public boolean onExecution(String[] args, CommandIssuer issuer) {
        if (args.length > 0) {
            ClassicBukkit.getServer().admins.removePlayer(args[0]);
            issuer.sendChatMessage(args[0] + " is no longer op.");
        } else {
            issuer.sendChatMessage(ChatColor.RED + "Usage: /deop <player>");
        }
        return false;
    }
}
