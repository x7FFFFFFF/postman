package org.my.bus;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an event handling method as being thread-safe.  This annotation
 * indicates that EventBus may invoke the event handler simultaneously from
 * multiple threads.
 *
 * <p>This does not mark the method as an event handler, and so should be used
 * in combination with {@link Subscribe}.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface AllowConcurrentEvents {
}