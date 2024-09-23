package met.realfreehij.classicbukkit.plugins.events.listeners;

import met.realfreehij.classicbukkit.plugins.events.EventPlayerJoin;
import met.realfreehij.classicbukkit.plugins.events.EventPlayerQuit;

public interface ListenerJoinQuit extends Listener {
    void onPlayerJoin(EventPlayerJoin eventPlayerJoin);

    void onPlayerLeave(EventPlayerQuit eventPlayerQuit);
}
