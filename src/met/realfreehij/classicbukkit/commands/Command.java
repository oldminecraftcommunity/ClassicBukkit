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

    public boolean onExecution(String[] args, PlayerInstance player) {
        return false;
    }
}
