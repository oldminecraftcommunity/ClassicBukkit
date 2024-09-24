package met.realfreehij.classicbukkit.plugins;

import com.mojang.minecraft.server.MinecraftServer;
import met.realfreehij.classicbukkit.plugins.events.*;
import met.realfreehij.classicbukkit.plugins.events.listeners.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class PluginManager {
    private final ArrayList<PluginEntry> plugins = new ArrayList<>();
    private final ArrayList<Listener> listeners = new ArrayList<>();

    public void loadPlugin(File jarFile) {
        try {
            URL[] urls = {jarFile.toURI().toURL()};
            URLClassLoader classLoader = new URLClassLoader(urls);

            String name = null;
            String version = null;
            String author = null;
            String main = null;

            InputStream inputStream = classLoader.getResourceAsStream("plugin.yml");
            if(inputStream == null) {
                System.out.println("plugin.yml not found in " + jarFile.getName());
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if(parts.length != 2) continue;

                String key = parts[0];
                String value = parts[1];

                switch(key) {
                    case "name":
                        name = value.trim();
                        break;
                    case "version":
                        version = value.trim();
                        break;
                    case "author":
                        author = value.trim();
                        break;
                    case "main":
                        main = value.trim();
                        break;
                }
            }
            
            if(name == null) {
                System.out.println("no name in plugin.yml in " + jarFile.getName());
                return;
            }
            if(version == null) {
                System.out.println("no plugin version in plugin.yml in " + jarFile.getName());
                return;
            }
            if(author == null) {
                System.out.println("no author in plugin.yml in " + jarFile.getName());
                return;
            }
            if(main == null) {
                System.out.println("no main class in plugin.yml in " + jarFile.getName());
                return;
            }

            Class<?> clazz = classLoader.loadClass(main);
            if(Plugin.class.isAssignableFrom(clazz)) {
                Plugin plugin = (Plugin)clazz.newInstance();
                MinecraftServer.logger.info("Loaded plugin " + name + " v" + version + " by " + author);
                plugins.add(new PluginEntry(
                        plugin,
                        name,
                        version,
                        author
                ));
                plugin.onEnable();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void unLoadPlugins() {
        for(PluginEntry pluginEntry : plugins) {
            pluginEntry.getPlugin().onDisable();
        }
    }

    public ArrayList<PluginEntry> getLoadedPlugins() {
        return plugins;
    }

    public PluginEntry getPlugin(Plugin plugin) {
        for(PluginEntry pluginEntry : plugins) {
            if(pluginEntry.getPlugin().equals(plugin)) return pluginEntry;
        }
        return null;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void fireChatMessage(EventChatMessage event) {
        for(Listener listener : listeners) {
            if(listener instanceof ListenerChat) {
                ((ListenerChat)listener).onChatMessage(event);
            }
        }
    }

    public void fireSetTile(EventSetTilePlayer event) {
        for(Listener listener : listeners) {
            if(listener instanceof ListenerTile) {
                ((ListenerTile)listener).onSetTile(event);
            }
        }
    }

    public void firePlayerJoin(EventPlayerJoin event) {
        for(Listener listener : listeners) {
            if(listener instanceof ListenerJoinQuit) {
                ((ListenerJoinQuit)listener).onPlayerJoin(event);
            }
        }
    }

    public void firePlayerQuit(EventPlayerQuit event) {
        for(Listener listener : listeners) {
            if(listener instanceof ListenerJoinQuit) {
                ((ListenerJoinQuit)listener).onPlayerLeave(event);
            }
        }
    }

    public void firePlayerMovement(EventPlayerMovement event) {
        for(Listener listener : listeners) {
            if(listener instanceof ListenerMovement) {
                ((ListenerMovement)listener).onPlayerMove(event);
            }
        }
    }
}
