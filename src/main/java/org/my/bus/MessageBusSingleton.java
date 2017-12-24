package org.my.bus;

/**
 * Created on 24.12.2017.
 */
public enum  MessageBusSingleton {
    INSTANCE(new EventBus());
    private final IEventBus eventBus;
    MessageBusSingleton(IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public IEventBus get() {
        return eventBus;
    }
}
