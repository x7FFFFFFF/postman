package org.my.view.left;

import org.my.bus.MessageBus;
import org.my.view.BasePanel;

import javax.swing.*;

/**
 * Created by paramonov on 17.08.17.
 */
public class LeftPanel extends BasePanel {

    public LeftPanel() {

        JTabbedPane tabbedPane = new JTabbedPane();
        FavoritsPanel favoritsPane = new FavoritsPanel();
        MessageBus.INSTANCE.publishListener(favoritsPane);
        tabbedPane.addTab(favoritsPane.getTranslatedName(), null, favoritsPane,
                "Does nothing");

        addOnePane(tabbedPane);

    }


}