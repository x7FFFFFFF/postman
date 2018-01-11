package org.my.view.right;

import org.my.bus.MessageBusSingleton;
import org.my.bus.Subscribe;
import org.my.controller.left.FavoritsPanelController;
import org.my.controller.left.RequestNode;
import org.my.controller.right.request.RequestPanelController;
import org.my.view.BasePanel;
import org.my.view.right.request.RequestSourcePanel;
import org.my.view.right.response.ResponseSourcePanel;

import javax.swing.JTabbedPane;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by paramonov on 17.08.17.
 */
public class RightPanel extends BasePanel  {
    private JTabbedPane tabbedPane = new JTabbedPane();
    private Map<Integer,RequestNode> openedEditors  = new HashMap<>();
    public RightPanel() {



        addOnePane(tabbedPane);



    }

    private Component createTab( RequestNode requestNode ){
        RequestSourcePanel requestPane = new RequestSourcePanel(requestNode);
        MessageBusSingleton.INSTANCE.get().register(requestPane);
        ResponseSourcePanel responsePane = new ResponseSourcePanel();
        MessageBusSingleton.INSTANCE.get().register(responsePane);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(requestPane.getTranslatedName(), null, requestPane,
                requestPane.getTranslatedName());

        tabbedPane.addTab(responsePane.getTranslatedName(), null, responsePane,
                responsePane.getTranslatedName());

        return tabbedPane;
    }


    @Subscribe
    public void onEvent(FavoritsPanelController.LoadRequestFromTreeEvent event) {
            RequestNode requestNode = event.getTarget();
            if (openedEditors.containsKey(requestNode.getRow())){

            } else {

                Component tab = createTab(requestNode);
                //TODO:
                tabbedPane.add(requestNode.getName(), tab);
            }
    }
}