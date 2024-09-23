package met.realfreehij.classicbukkit.plugins.events;

import com.mojang.minecraft.server.PlayerInstance;

public class EventPlayerMovement extends Event {
    private final PlayerInstance player;

    public EventPlayerMovement(PlayerInstance player) {
        this.player = player;
    }

    public PlayerInstance getPlayer() {
        return player;
    }
}
