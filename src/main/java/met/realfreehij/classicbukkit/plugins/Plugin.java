package met.realfreehij.classicbukkit.plugins;

import met.realfreehij.classicbukkit.ClassicBukkit;
import met.realfreehij.classicbukkit.commands.Command;
import met.realfreehij.classicbukkit.plugins.events.listeners.Listener;

import java.io.File;

public abstract class Plugin {
    public abstract void onEnable();

    public abstract void onDisable();

    public void addListener(Listener listener) {
        ClassicBukkit.pluginManager.addListener(listener);
    }

    public void addCommand(Command command) {
        ClassicBukkit.commandManager.addCommand(command);
    }

    public String getPluginDir() {
        new File("plugins/" + ClassicBukkit.pluginManager.getPlugin(this).getName() + "/").mkdir();
        return "plugins/" + ClassicBukkit.pluginManager.getPlugin(this).getName() + "/";
    }
}
