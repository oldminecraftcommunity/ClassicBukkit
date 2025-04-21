package met.realfreehij.classicbukkit.plugins;

public class PluginEntry {
    private final Plugin plugin;
    private final String name;
    private final String version;
    private final String author;

    public PluginEntry(Plugin plugin, String name, String version, String author) {
        this.plugin = plugin;
        this.name = name;
        this.version = version;
        this.author = author;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }
}
