package org.my.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dispatches events to listeners, and provides ways for listeners to register
 * themselves.
 *
 * <p>The EventBus allows publish-subscribe-style communication between
 * components without requiring the components to explicitly register with one
 * another (and thus be aware of each other).  It is designed exclusively to
 * replace traditional Java in-process event distribution using explicit
 * registration. It is <em>not</em> a general-purpose publish-subscribe system,
 * nor is it intended for interprocess communication.
 *
 * <h2>Receiving Events</h2>
 * To receive events, an object should:<ol>
 * <li>Expose a public method, known as the <i>event handler</i>, which accepts
 *     a single argument of the type of event desired;</li>
 * <li>Mark it with a {@link Subscribe} annotation;</li>
 * <li>Pass itself to an EventBus instance's {@link #register(Object)} method.
 *     </li>
 * </ol>
 *
 * <h2>Posting Events</h2>
 * To post an event, simply provide the event object to the
 * {@link #post(Object)} method.  The EventBus instance will determine the type
 * of event and route it to all registered listeners.
 *
 * <p>Events are routed based on their type &mdash; an event will be delivered
 * to any handler for any type to which the event is <em>assignable.</em>  This
 * includes implemented interfaces, all superclasses, and all interfaces
 * implemented by superclasses.
 *
 * <p>When {@code post} is called, all registered handlers for an event are run
 * in sequence, so handlers should be reasonably quick.
 *
 * <h2>Handler Methods</h2>
 * Event handler methods must accept only one argument: the event.
 *
 * <p>Handlers should not, in general, throw.  If they do, the EventBus will
 * catch and log the exception.  This is rarely the right solution for error
 * handling and should not be relied upon; it is intended solely to help find
 * problems during development.
 *
 * <p>The EventBus guarantees that it will not call a handler method from
 * multiple threads simultaneously, unless the method explicitly allows it by
 * bearing the {@link AllowConcurrentEvents} annotation.  If this annotation is
 * not present, handler methods need not worry about being reentrant, unless
 * also called from outside the EventBus.
 *
 * <h2>Dead Events</h2>
 * If an event is posted, but no registered handlers can accept it, it is
 * considered "dead."  To give the system a second chance to handle dead events,
 * they are wrapped in an instance of {@link DeadEvent} and reposted.
 *
 * <p>If a handler for a supertype of all events (such as Object) is registered,
 * no event will ever be considered dead, and no DeadEvents will be generated.
 * Accordingly, while DeadEvent extends {@link Object}, a handler registered to
 * receive any Object will never receive a DeadEvent.
 *
 * <p>This class is safe for concurrent use.
 *
 */
public class EventBus implements IEventBus {



    /**
     * All registered event handlers, indexed by event type.
     *
     * <p>All access should be
     * made after acquiring a read or write lock via {@link #handlersByTypeLock}.
     */
    private final Map<Class<?>, Set<EventHandler>> handlersByType =
            new HashMap<>();
    private final ReadWriteLock handlersByTypeLock = new ReentrantReadWriteLock();
    /**
     * Logger for event dispatch failures.  Named by the fully-qualified name of
     * this class, followed by the identifier provided at construction.
     */
    private final Logger logger;

    /** queues of events for the current thread to dispatch */
    private final ThreadLocal<Queue<EventWithHandler>> eventsToDispatch =
            ThreadLocal.withInitial(LinkedList::new);

    /** true if the current thread is currently dispatching an event */
    private final ThreadLocal<Boolean> isDispatching =
            ThreadLocal.withInitial(() -> false);

    /**
     * Creates a new EventBus named "default".
     */
    public EventBus() {
        this("default");
    }

    /**
     * Creates a new EventBus with the given {@code identifier}.
     *
     * @param identifier  a brief name for this bus, for logging purposes.  Should
     *                    be a valid Java identifier.
     */
    public EventBus(String identifier) {
        if (identifier==null){
            throw new NullPointerException();
        }
        logger = Logger.getLogger(EventBus.class.getName() + "." + identifier);
    }

    /**
     * Registers all handler methods on {@code object} to receive events.
     *
     * @param object  object whose handler methods should be registered.
     */
    @Override
    public void register(Object object) {
        final Map<Class<?>, Set<EventHandler>> allHandlers = findAllHandlers(object);
        handlersByTypeLock.writeLock().lock();
        try {
            handlersByType.putAll(allHandlers);
        } finally {
            handlersByTypeLock.writeLock().unlock();
        }
    }

    /**
     * Unregisters all handler methods on a registered {@code object}.
     *
     * @param object  object whose handler methods should be unregistered.
     * @throws IllegalArgumentException if the object was not previously registered.
     */
    @Override
    public void unregister(Object object) {
        final Map<Class<?>, Set<EventHandler>> allHandlers = findAllHandlers(object);
        for (Map.Entry<Class<?>, Set<EventHandler>> entry : allHandlers.entrySet()) {
            Class<?> eventType = entry.getKey();
            Set<EventHandler> eventMethodsInListener = entry.getValue();

            handlersByTypeLock.writeLock().lock();
            try {
                Set<EventHandler> currentHandlers = handlersByType.get(eventType);
                if (!currentHandlers.containsAll(eventMethodsInListener)) {
                    throw new IllegalArgumentException(
                            "missing event handler for an annotated method. Is " + object + " registered?");
                }
                currentHandlers.removeAll(eventMethodsInListener);
            } finally {
                handlersByTypeLock.writeLock().unlock();
            }
        }
    }


    /**
     * Posts an event to all registered handlers.  This method will return
     * successfully after the event has been posted to all handlers, and
     * regardless of any exceptions thrown by handlers.
     *
     * <p>If no handlers have been subscribed for {@code event}'s class, and
     * {@code event} is not already a {@link DeadEvent}, it will be wrapped in a
     * DeadEvent and reposted.
     *
     * @param event  event to post.
     */
    @Override
    public void post(Object event) {
        Class<?> eventType = event.getClass();

        boolean dispatched = false;
        handlersByTypeLock.readLock().lock();
        try {
            Set<EventHandler> wrappers = handlersByType.get(eventType);

            if (!wrappers.isEmpty()) {
                dispatched = true;
                for (EventHandler wrapper : wrappers) {
                    enqueueEvent(event, wrapper);
                }
            }
        } finally {
            handlersByTypeLock.readLock().unlock();
        }


        if (!dispatched && !(event instanceof DeadEvent)) {
            post(new DeadEvent(this, event));
        }

        dispatchQueuedEvents();
    }

    /**
     * Queue the {@code event} for dispatch during
     * {@link #dispatchQueuedEvents()}. Events are queued in-order of occurrence
     * so they can be dispatched in the same order.
     */
    void enqueueEvent(Object event, EventHandler handler) {
        eventsToDispatch.get().offer(new EventWithHandler(event, handler));
    }

    /**
     * Drain the queue of events to be dispatched. As the queue is being drained,
     * new events may be posted to the end of the queue.
     */
    void dispatchQueuedEvents() {
        // don't dispatch if we're already dispatching, that would allow reentrancy
        // and out-of-order events. Instead, leave the events to be dispatched
        // after the in-progress dispatch is complete.
        if (isDispatching.get()) {
            return;
        }

        isDispatching.set(true);
        try {
            Queue<EventWithHandler> events = eventsToDispatch.get();
            EventWithHandler eventWithHandler;
            while ((eventWithHandler = events.poll()) != null) {
                dispatch(eventWithHandler.event, eventWithHandler.handler);
            }
        } finally {
            isDispatching.remove();
            eventsToDispatch.remove();
        }
    }

    /**
     * Dispatches {@code event} to the handler in {@code wrapper}.  This method
     * is an appropriate override point for subclasses that wish to make
     * event delivery asynchronous.
     *
     * @param event  event to dispatch.
     * @param wrapper  wrapper that will call the handler.
     */
    void dispatch(Object event, EventHandler wrapper) {
        try {
            wrapper.handleEvent(event);
        } catch (InvocationTargetException e) {
            logger.log(Level.SEVERE,
                    "Could not dispatch event: " + event + " to handler " + wrapper, e);
        }
    }



    private Map<Class<?>, Set<EventHandler>> findAllHandlers(Object listener) {
        Map<Class<?>, Set<EventHandler>> methodsInListener = new HashMap<>();

        Class<?> clazz = listener.getClass();
        for (Method method : getAnnotatedMethodsInternal(clazz)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];
            EventHandler handler = makeHandler(listener, method);
            final Set<EventHandler> eventHandlerSet = methodsInListener.get(eventType);
            if (eventHandlerSet!=null){
                eventHandlerSet.add(handler);
            } else {
                Set<EventHandler> set = new HashSet<>();
                set.add(handler);
                methodsInListener.put(eventType, set);
            }

        }
        return methodsInListener;
    }
    private static List<Method> getAnnotatedMethodsInternal(Class<?> clazz) {
        List<Method> result = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Subscribe.class)) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length != 1) {
                            throw new IllegalArgumentException("Method " + method
                                    + " has @Subscribe annotation, but requires " + parameterTypes.length
                                    + " arguments.  Event handler methods must require a single argument.");
                        }

                        result.add(method);
                        //break;
                    }

        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Creates an {@code EventHandler} for subsequently calling {@code method} on
     * {@code listener}.
     * Selects an EventHandler implementation based on the annotations on
     * {@code method}.
     *
     * @param listener  object bearing the event handler method.
     * @param method  the event handler method to wrap in an EventHandler.
     * @return an EventHandler that will call {@code method} on {@code listener}
     *         when invoked.
     */
    private static EventHandler makeHandler(Object listener, Method method) {
        EventHandler wrapper;
        if (methodIsDeclaredThreadSafe(method)) {
            wrapper = new EventHandler(listener, method);
        } else {
            wrapper = new SynchronizedEventHandler(listener, method);
        }
        return wrapper;
    }

    /**
     * Checks whether {@code method} is thread-safe, as indicated by the
     * {@link AllowConcurrentEvents} annotation.
     *
     * @param method  handler method to check.
     * @return {@code true} if {@code handler} is marked as thread-safe,
     *         {@code false} otherwise.
     */
    private static boolean methodIsDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }



    /** simple struct representing an event and it's handler */
    static class EventWithHandler {
        final Object event;
        final EventHandler handler;
        EventWithHandler(Object event, EventHandler handler) {
            if (event==null || handler==null){
                throw new NullPointerException();
            }

            this.event = event;
            this.handler = handler;
        }
    }


}