package met.realfreehij.classicbukkit.plugins.events;

import com.mojang.minecraft.server.PlayerInstance;

public class EventChatMessage extends Event {
    private final String message;
    private final PlayerInstance player;

    public EventChatMessage(String message, PlayerInstance player) {
        this.message = message;
        this.player = player;
    }

    public String getMessage() {
        return message;
    }

    public PlayerInstance getPlayer() {
        return player;
    }
}
