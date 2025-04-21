package met.realfreehij.classicbukkit.plugins.events;

import com.mojang.minecraft.server.PlayerInstance;

public class EventPlayerQuit extends Event {
    private final PlayerInstance player;

    public EventPlayerQuit(PlayerInstance player) {
        this.player = player;
    }

    public PlayerInstance getPlayer() {
        return player;
    }
}
