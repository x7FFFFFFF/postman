package org.my.view.right.response;

import org.my.controller.right.request.RequestPanelController;
import org.my.helpers.UiHelper;
import org.my.bus.Message;
import org.my.bus.MessageBus;
import org.my.http.client.HttpResponse;
import org.my.view.BasePanel;
import org.my.view.StatusBarPanel;

import javax.swing.GroupLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.Color;

/**
 * Created by paramonov on 17.08.17.
 */
public class ResponseSourcePanel extends BasePanel {

    public static final Color COLOR_GET_RESPONSE = Color.green;
    private Color savedColor;

    private JLabel responseHeadersLabel = new JLabel("Headers:");
    private  JEditorPane responseHeadersTextPane = new JEditorPane();
    private final JScrollPane responseHeadersScrollPane = new JScrollPane(responseHeadersTextPane);

    private JLabel responseBodyLabel = new JLabel("Body:");
    private JEditorPane responseBodyTextPane = new JEditorPane();
    private final JScrollPane responseBodyScrollPane = new JScrollPane(responseBodyTextPane);

    public ResponseSourcePanel() {

        JSplitPane splitPane = UiHelper.createSplitPane(JSplitPane.VERTICAL_SPLIT, createUpperPanel(), createBottomPanel());
        addOnePane(splitPane);
        MessageBus.INSTANCE.publishListener(this::onEvent);
    }



    private JPanel createBottomPanel() {
        BasePanel basePanel = new BasePanel();
        basePanel.setBackground(null);
        GroupLayout groupLayout = basePanel.getBaseLayout();
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
                .addComponent(responseBodyLabel)
                .addComponent(responseBodyScrollPane)
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(responseBodyLabel)
                        .addComponent(responseBodyScrollPane)
        );
        return basePanel;
    }

    private JPanel createUpperPanel() {
        BasePanel basePanel = new BasePanel();
        basePanel.setBackground(null);
        GroupLayout groupLayout = basePanel.getBaseLayout();
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
                .addComponent(responseHeadersLabel)
                .addComponent(responseHeadersScrollPane)
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(responseHeadersLabel)
                        .addComponent(responseHeadersScrollPane)
        );
        return basePanel;
    }





    private void onEvent(Message message) {
        if (message.getId()== RequestPanelController.Actions.REQUEST_COMPLETED){
           HttpResponse response = message.getSource();
            responseBodyTextPane.setText(response.getBody());
            responseHeadersTextPane.setText(response.getHeadersStr());
            StatusBarPanel.setStatusBarMsg("Ответ получен!");
        }

    }
}