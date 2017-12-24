package org.my.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Marks a method as an event handler,
 *
 * <p>The type of event will be indicated by the method's first (and only)
 * parameter.  If this annotation is applied to methods with zero parameters,
 * or more than one parameter, the object containing the method will not be able
 * to register for event delivery from the {@link EventBus}.
 *
 * <p>Unless also annotated with @{@link AllowConcurrentEvents}, event handler
 * methods will be invoked serially by each event bus that they are registered
 * with.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
}
