package met.realfreehij.classicbukkit.plugins.events.listeners;

import met.realfreehij.classicbukkit.plugins.events.EventPlayerMovement;

public interface ListenerMovement extends Listener {
    void onPlayerMove(EventPlayerMovement event);
}
