package met.realfreehij.classicbukkit.plugins.events.listeners;

import met.realfreehij.classicbukkit.plugins.events.EventChatMessage;

public interface ListenerChat extends Listener {
    void onChatMessage(EventChatMessage event);
}
