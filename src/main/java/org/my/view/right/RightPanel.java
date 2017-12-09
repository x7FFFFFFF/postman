package org.my.view.right;

import org.my.bus.IBusEventListener;
import org.my.bus.Message;
import org.my.bus.MessageBus;
import org.my.controller.left.FavoritsPanelController;
import org.my.controller.left.RequestNode;
import org.my.controller.right.request.RequestPanelController;
import org.my.view.BasePanel;
import org.my.view.right.request.RequestSourcePanel;
import org.my.view.right.response.ResponseSourcePanel;

import javax.swing.JTabbedPane;
import java.awt.*;

/**
 * Created by paramonov on 17.08.17.
 */
public class RightPanel extends BasePanel implements IBusEventListener {
    JTabbedPane tabbedPane = new JTabbedPane();
    public RightPanel() {


       /* JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                requestPane, responsePane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(500);
        UiHelper.flattenSplitPane(splitPane);*/
        MessageBus.INSTANCE.publishListener(RequestPanelController.INSTANCE);






        addOnePane(tabbedPane);

      /*  final Component requestSourcePanel = UiHelper.getChildNamed(requestPane, RequestSourcePanel.class.getCanonicalName());

        final PropertyChangeListener responseSourcePanel = (PropertyChangeListener)UiHelper.getChildNamed(responsePane, ResponseSourcePanel.class.getCanonicalName());

        requestSourcePanel.addPropertyChangeListener(responseSourcePanel);*/

    }

    private Component createTab( RequestNode requestNode ){
        RequestSourcePanel requestPane = new RequestSourcePanel(requestNode);
        BasePanel responsePane = new ResponseSourcePanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(requestPane.getTranslatedName(), null, requestPane,
                requestPane.getTranslatedName());

        tabbedPane.addTab(responsePane.getTranslatedName(), null, responsePane,
                responsePane.getTranslatedName());

        MessageBus.INSTANCE.publishListener(requestPane);
        return tabbedPane;
    }


    @Override
    public void onEvent(Message message) {
        if (message.getId() == FavoritsPanelController.Actions.LOAD_REQUEST_FROM_TREE) {
            RequestNode requestNode = message.getSource();
            if (requestNode.getRequest()!=null) {
                Component tab = createTab(requestNode);
            //TODO:
            }
        }
    }
}