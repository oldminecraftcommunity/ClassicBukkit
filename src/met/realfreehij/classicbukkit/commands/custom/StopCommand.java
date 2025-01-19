package met.realfreehij.classicbukkit.commands.custom;

import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.commands.CommandIssuer;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "Stop the server", new String[] {}, true);
    }

    @Override
    public boolean onExecution(String[] args, CommandIssuer issuer) {
        issuer.sendChatMessage("Stopping server...");
        ClassicBukkit.getServer().shutdownServer();
        return false;
    }
}