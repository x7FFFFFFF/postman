package org.my.view.right.request;

import org.my.controller.right.request.RequestPanelController;
import org.my.helpers.UiHelper;
import org.my.helpers.bus.Message;
import org.my.helpers.bus.MessageBus;
import org.my.http.client.HttpRequest;
import org.my.http.client.HttpRequestParseException;
import org.my.view.BasePanel;
import org.my.view.left.FavoritsPanel;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.Dimension;

/**
 * Created by paramonov on 17.08.17.
 */
public class RequestSourcePanel extends BasePanel {

    private final  JEditorPane requestHeadersPane = new JEditorPane();
    private final  JEditorPane requestBodyPane = new JEditorPane();

    private final  JButton sendButton = new JButton(translate("Send"));
    private final  JButton cancelButton = new JButton("Cancel");
    private final  JButton saveButton = new JButton("Save");

    public enum Actions {
        SEND_BUTTON_PUSHED,
        SAVE_BUTTON_PUSHED,
        DEACTIVATE_SEND_BUTTON,
        ACTIVATE_SEND_BUTTON
    }

    public RequestSourcePanel() {



        JSplitPane splitPane = UiHelper.createSplitPane(JSplitPane.VERTICAL_SPLIT, createUpperPanel(), createBottomPanel());
        addOnePane(splitPane);
       // sendButton.setAction(new SendRequestAction(requestHeadersPane.getDocument(), requestBodyPane.getDocument(), sendButton.getModel(), this ));
        sendButton.addActionListener(e -> {
            MessageBus.INSTANCE.sentMessage(Actions.SEND_BUTTON_PUSHED, getHttpRequest());
        });
        saveButton.addActionListener(e->{
            MessageBus.INSTANCE.sentMessage(Actions.SAVE_BUTTON_PUSHED,  getHttpRequest());
        });

        MessageBus.INSTANCE.publishListener(RequestPanelController.INSTANCE);

        MessageBus.INSTANCE.publishListener(this::onEvent);
    }

    public HttpRequest getHttpRequest() {
        HttpRequest httpRequest;
        try {
            httpRequest = new HttpRequest(requestHeadersPane.getText(), requestBodyPane.getText());
        } catch (HttpRequestParseException e1) {
            e1.printStackTrace();
            throw  new RuntimeException(e1);
            //TODO: handle
        }
        return httpRequest;
    }

    private void onEvent(Message message) {
        if (message.getId()==Actions.DEACTIVATE_SEND_BUTTON){
            sendButton.setEnabled(false);
        } else if (message.getId()==Actions.ACTIVATE_SEND_BUTTON){
            sendButton.setEnabled(true);
        } else if (message.getId() == FavoritsPanel.Actions.LOAD_REQUEST_FROM_TREE) {
            HttpRequest request = message.getSource();
            requestHeadersPane.setText(request.getSource());
            requestBodyPane.setText(request.getBody());
        }

    }

    private static class RequestUpperPanel extends BasePanel {

    }
    private static class RequestBottomPanel extends BasePanel {

    }
    private JPanel createUpperPanel() {
        RequestUpperPanel panel = new RequestUpperPanel();

        final JScrollPane requestHeadersScrollPane = new JScrollPane(requestHeadersPane);
        requestHeadersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        requestHeadersScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        final JLabel labelHeader = new JLabel("Headers:");
        requestHeadersPane.setPreferredSize(new Dimension(250, 145));
        requestHeadersPane.setMinimumSize(new Dimension(10, 10));
        GroupLayout layout = panel.getBaseLayout();
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(labelHeader)
                .addComponent(requestHeadersScrollPane)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(labelHeader)
                .addComponent(requestHeadersScrollPane)
        );


        return panel;
    }
    private JPanel createBottomPanel() {
        RequestBottomPanel panel = new RequestBottomPanel();
         final JLabel labelBody =new JLabel("Body:");
        final JScrollPane requestBodyPaneScrollPane = new JScrollPane(requestBodyPane);
        requestBodyPaneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        requestBodyPaneScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        requestBodyPane.setPreferredSize(new Dimension(250, 145));
        requestBodyPane.setMinimumSize(new Dimension(10, 10));
        GroupLayout layout = panel.getBaseLayout();
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(labelBody)
                .addComponent(requestBodyPaneScrollPane)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(saveButton)
                        .addComponent(cancelButton)
                        .addComponent(sendButton)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(labelBody)
                .addComponent(requestBodyPaneScrollPane)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(saveButton)
                        .addComponent(cancelButton)
                        .addComponent(sendButton)
                )
        );

        return panel;
    }




}