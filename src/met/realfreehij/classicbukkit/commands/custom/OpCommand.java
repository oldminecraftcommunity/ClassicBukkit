package met.realfreehij.classicbukkit.commands.custom;

import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.commands.CommandIssuer;
import met.realfreehij.classicbukkit.utils.ChatColor;
import met.realfreehij.classicbukkit.ClassicBukkit;

public class OpCommand extends Command {
    public OpCommand() {
        super("op", "Make player an operator", new String[] {}, true);
    }

    @Override
    public boolean onExecution(String[] args, CommandIssuer issuer) {
        if (args.length > 0) {
            ClassicBukkit.getServer().admins.addPlayer(args[0]);
            issuer.sendChatMessage(args[0] + " is now op.");
        } else {
            issuer.sendChatMessage(ChatColor.RED + "Usage: /op <player>");
        }
        return false;
    }
}
