package org.my.bus;

/**
 * Created on 24.12.2017.
 */
public class TargetEvent<T> {
    final T target;

    public TargetEvent(T target) {
        this.target = target;
    }



    public T getTarget() {
        return target;
    }
}
