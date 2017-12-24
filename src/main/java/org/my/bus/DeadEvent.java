package org.my.bus;

/**
 * Wraps an event that was posted, but which had no subscribers and thus could
 * not be delivered.
 *
 * <p>Subscribing a DeadEvent handler is useful for debugging or logging, as it
 * can detect misconfigurations in a system's event distribution.
 *
 */
public class DeadEvent {
    private final Object source;
    private final Object event;

    /**
     * Creates a new DeadEvent.
     *
     * @param source  object broadcasting the DeadEvent (generally the
     *                {@link EventBus}).
     * @param event   the event that could not be delivered.
     */
    public DeadEvent(Object source, Object event) {
        if (source==null || event==null){
            throw new NullPointerException();
        }
        this.source = source;
        this.event = event;
    }

    /**
     * Returns the object that originated this event (<em>not</em> the object that
     * originated the wrapped event).  This is generally an {@link EventBus}.
     *
     * @return the source of this event.
     */
    public Object getSource() {
        return source;
    }

    /**
     * Returns the wrapped, 'dead' event, which the system was unable to deliver
     * to any registered handler.
     *
     * @return the 'dead' event that could not be delivered.
     */
    public Object getEvent() {
        return event;
    }

}