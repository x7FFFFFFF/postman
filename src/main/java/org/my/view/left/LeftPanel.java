package org.my.view.left;

import org.my.view.BasePanel;

import javax.swing.*;

/**
 * Created by paramonov on 17.08.17.
 */
public class LeftPanel extends BasePanel {

    public LeftPanel() {

        JTabbedPane tabbedPane = new JTabbedPane();
        BasePanel favoritsPane = new FavoritsPanel();
        tabbedPane.addTab(favoritsPane.getTranslatedName(), null, favoritsPane,
                "Does nothing");

        addOnePane(tabbedPane);

    }


}