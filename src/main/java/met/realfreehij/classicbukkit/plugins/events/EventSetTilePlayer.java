package met.realfreehij.classicbukkit.plugins.events;

import com.mojang.minecraft.server.PlayerInstance;

public class EventSetTilePlayer extends Event {
    private short x;
    private short y;
    private short z;
    private byte id;
    private final PlayerInstance player;

    public EventSetTilePlayer(final short x, final short y, final short z, final byte id, final PlayerInstance player) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
        this.player = player;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public short getZ() {
        return z;
    }

    public byte getId() {
        return id;
    }

    public PlayerInstance getPlayer() {
        return player;
    }

    public EventSetTilePlayer setX(final short x) {
        this.x = x;
        return this;
    }

    public EventSetTilePlayer setY(final short y) {
        this.y = y;
        return this;
    }

    public EventSetTilePlayer setZ(final short z) {
        this.z = z;
        return this;
    }

    public EventSetTilePlayer setId(final byte id) {
        this.id = id;
        return this;
    }
}
