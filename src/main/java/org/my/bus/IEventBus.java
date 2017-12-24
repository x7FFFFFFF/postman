package org.my.bus;

/**
 * Created by paramonov on 19.12.17.
 */
public interface IEventBus {
    void register(Object object);

    void unregister(Object object);

    void post(Object event);


}