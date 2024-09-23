package met.realfreehij.classicbukkit.plugins.events;

public abstract class Event {
    private boolean cancelled = false;

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
