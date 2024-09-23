package met.realfreehij.classicbukkit.plugins.events;

import com.mojang.minecraft.server.PlayerInstance;

public class EventPlayerJoin extends Event {
    private final PlayerInstance player;

    public EventPlayerJoin(PlayerInstance player) {
        this.player = player;
    }

    public PlayerInstance getPlayer() {
        return player;
    }
}
