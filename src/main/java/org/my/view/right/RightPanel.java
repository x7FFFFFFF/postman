package org.my.view.right;

import org.my.view.BasePanel;
import org.my.view.right.request.RequestSourcePanel;
import org.my.view.right.response.ResponseSourcePanel;

import javax.swing.JTabbedPane;

/**
 * Created by paramonov on 17.08.17.
 */
public class RightPanel extends BasePanel {

    public RightPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();

       /* JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                requestPane, responsePane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(500);
        UiHelper.flattenSplitPane(splitPane);*/


        BasePanel requestPane = new RequestSourcePanel();
        BasePanel responsePane = new ResponseSourcePanel();

        tabbedPane.addTab(requestPane.getTranslatedName(), null, requestPane,
                requestPane.getTranslatedName());

        tabbedPane.addTab(responsePane.getTranslatedName(), null, responsePane,
                responsePane.getTranslatedName());
        addOnePane(tabbedPane);

      /*  final Component requestSourcePanel = UiHelper.getChildNamed(requestPane, RequestSourcePanel.class.getCanonicalName());

        final PropertyChangeListener responseSourcePanel = (PropertyChangeListener)UiHelper.getChildNamed(responsePane, ResponseSourcePanel.class.getCanonicalName());

        requestSourcePanel.addPropertyChangeListener(responseSourcePanel);*/

    }



}