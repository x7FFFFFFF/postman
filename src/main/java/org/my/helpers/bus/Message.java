package org.my.helpers.bus;



public final class Message {
    private final  Object source;
    private final Enum id;

    public Message( Enum id, Object source) {
        this.source = source;
        this.id = id;
    }

    @SuppressWarnings("unchecked")
    public <T> T getSource() {
        return (T)source;
    }

    public Enum getId() {
        return id;
    }


}