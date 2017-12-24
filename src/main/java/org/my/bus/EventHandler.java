package org.my.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wraps a single-argument 'handler' method on a specific object.
 *
 * <p>This class only verifies the suitability of the method and event type if
 * something fails.  Callers are expected to verify their uses of this class.
 *
 * <p>Two EventHandlers are equivalent when they refer to the same method on the
 * same object (not class).   This property is used to ensure that no handler
 * method is registered more than once.
 *
 */
public class EventHandler {
    /** Object sporting the handler method. */
    private final Object target;
    /** Handler method. */
    private final Method method;

    /**
     * Creates a new EventHandler to wrap {@code method} on @{code target}.
     *
     * @param target  object to which the method applies.
     * @param method  handler method.
     */
    public EventHandler(Object target, Method method) {
        if (target == null || method == null) {
            throw new NullPointerException();
        }
        this.target = target;
        this.method = method;
        method.setAccessible(true);
    }


    /**
     * Invokes the wrapped handler method to handle {@code event}.
     *
     * @param event  event to handle
     * @throws InvocationTargetException  if the wrapped method throws any
     *     {@link Throwable} that is not an {@link Error} ({@code Error} instances are
     *     propagated as-is).
     */
    public void handleEvent(Object event) throws InvocationTargetException {
        if (event == null) {
            throw new NullPointerException();
        }
        try {
            method.invoke(target, event);
        } catch (IllegalArgumentException e) {
            throw new Error("Method rejected target/argument: " + event, e);
        } catch (IllegalAccessException e) {
            throw new Error("Method became inaccessible: " + event, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            throw e;
        }
    }


    @Override public String toString() {
        return "[wrapper " + method + "]";
    }

    @Override public int hashCode() {
        final int PRIME = 31;
        return (PRIME + method.hashCode()) * PRIME
                + System.identityHashCode(target);
    }

    @Override public boolean equals(Object obj) {
        if (obj instanceof EventHandler) {
            EventHandler that = (EventHandler) obj;
            // Use == so that different equal instances will still receive events.
            // We only guard against the case that the same object is registered
            // multiple times
            return target == that.target && method.equals(that.method);
        }
        return false;
    }

}