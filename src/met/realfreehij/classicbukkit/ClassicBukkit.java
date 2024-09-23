package met.realfreehij.classicbukkit;

import com.mojang.minecraft.server.MinecraftServer;
import met.realfreehij.classicbukkit.commands.CommandManager;
import met.realfreehij.classicbukkit.plugins.PluginManager;

import java.io.File;

public class ClassicBukkit {
    private static MinecraftServer server;
    public static final PluginManager pluginManager = new PluginManager();
    public static final CommandManager commandManager = new CommandManager();
    public static final String name = "ClassicBukkit";
    public static final String version = "1.0alpha";

    public ClassicBukkit(MinecraftServer server) {
        ClassicBukkit.server = server;

        File pluginFolder = new File("plugins/");
        if(!pluginFolder.exists() || !pluginFolder.isDirectory()) {
            pluginFolder.mkdir();
        }
        File[] files = pluginFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
        for(File file : files) {
            pluginManager.loadPlugin(file);
        }
    }

    public static MinecraftServer getServer() {
        return server;
    }
}
