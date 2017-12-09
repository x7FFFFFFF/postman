package org.my.view;

import org.my.helpers.LocaleManager;
import org.my.helpers.bus.Message;
import org.my.helpers.bus.MessageBus;

import javax.swing.*;

/**
 * Created by paramonov on 23.08.17.
 */
public  class BasePanel extends JPanel {


    private GroupLayout layout;
    private  final String name = getClass().getCanonicalName();
    public BasePanel() {
        setName(name);
        setDefaultLayout();
        //MessageBus.get().publishListener(this::onEvent);

    }

    /*protected void onEvent(Message message) {

    }*/

    protected void setDefaultLayout() {
        layout =  new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);
    }

    public String getSystemName(){
        return  name;
    }

    public String getTranslatedName(){
        return  LocaleManager.get().translate(name);
    }

    public String translate(String value) {
        return  LocaleManager.get().translate(name + "." + value);
    }


    protected void addOnePane(JComponent comp){
        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(comp));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(comp));
    }


    public GroupLayout getBaseLayout() {
        return layout;
    }
}