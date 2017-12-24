/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: BOF VIP-Service
 *
 * $Id: Main.java 2017-12-19 13:49 paramonov $
 *****************************************************************/
package org.my.bus;

public class Main {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        EventListener listener = new EventListener();
        eventBus.register(listener);
        eventBus.post("String Event");
    }

}
class EventListener {

    private static int eventsHandled;

    @Subscribe
    public void stringEvent(String event) {
        eventsHandled++;
    }
    @Subscribe
    public void stringEvent2(String event) {
        eventsHandled++;
    }
}