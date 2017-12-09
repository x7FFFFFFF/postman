package org.my.controller.left;

/**
 * Created on 09.12.2017.
 */
public class Element {
    String name;

    public Element(String name) {

        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString()    {
        return name;
    }
}
