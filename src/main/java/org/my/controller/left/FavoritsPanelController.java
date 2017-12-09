package org.my.controller.left;

import org.my.helpers.bus.IBusEventListener;
import org.my.helpers.bus.Message;
import org.my.helpers.bus.MessageBus;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created on 19.11.2017.
 */
public enum FavoritsPanelController  implements IBusEventListener {

    INSTANCE;


    private TreeModel model;
    private TreeSelectionModel selectionModel;
    private MouseListener mouseListener = new MouseListener();
    private boolean initialized;

    FavoritsPanelController() {
        MessageBus.INSTANCE.publishListener(this);
    }

    public synchronized void init(TreeModel model, TreeSelectionModel selectionModel) {
        if (initialized) {
            throw new RuntimeException("already initialized");
        }
        this.initialized = true;
        this.model = model;
        this.selectionModel = selectionModel;
    }


    public class MouseListener extends MouseAdapter {

            @Override
            public void mouseClicked(MouseEvent e) {

            }
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    @Override
    public void onEvent(Message msg) {

    }
}
