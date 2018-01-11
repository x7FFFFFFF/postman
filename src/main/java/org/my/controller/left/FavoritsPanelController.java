package org.my.controller.left;

import org.my.bus.MessageBusSingleton;
import org.my.bus.Subscribe;
import org.my.bus.TargetEvent;
import org.my.http.client.HttpRequest;
import org.my.view.right.request.RequestSourcePanel;
//import org.my.view.left.FavoritsPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 19.11.2017.
 */
public enum FavoritsPanelController   {

    INSTANCE;
    private final Set<RequestNode> nodeSet = new HashSet<>();

    public static class PopupShowEvent extends TargetEvent<MouseEvent>{
        public PopupShowEvent(MouseEvent target) {
            super( target);
        }
    }

    public static class LoadRequestFromTreeEvent extends TargetEvent<RequestNode>{
        public LoadRequestFromTreeEvent(RequestNode target) {
            super( target);
        }
    }



    public enum Events {
        UPDATE_UI
    }
    public static final String CREATE_FOLDER = "createFolder";
    public static final String CREATE_REQUEST = "createRequest";
    public static final String REMOVE = "remove";


    private TreeModel model;
    private TreeSelectionModel selectionModel;
    private MouseListener mouseListener = new MouseListener();
    private PopupListener  popupListener  = new PopupListener();
    private boolean initialized;
    private DefaultMutableTreeNode currentFolderNode;
    private DefaultMutableTreeNode currentRequestNode;
    private DefaultMutableTreeNode rootNode;


    public synchronized void init(TreeModel model, TreeSelectionModel selectionModel) {
        this.rootNode = (DefaultMutableTreeNode) model.getRoot();
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
                setSelectedNodes();
                if (SwingUtilities.isRightMouseButton(e)){
                    treeMouseRightClicked(e);
                } else if (SwingUtilities.isLeftMouseButton(e)){
                    if (e.getClickCount() == 1) {

                    } else if (e.getClickCount() == 2) {
                       treeMouseLeftDoubleClicked(e);
                    }
                }
            }


    }
    private  class PopupListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case CREATE_FOLDER:
                    addFolder("New folder");
                    //MessageBus.INSTANCE.sentMessage(FavoritsPanel.Actions.POPUP_ADD_FOLDER);
                    break;

                case   CREATE_REQUEST:
                    RequestNode node = RequestNode.createNewEmpty();
                    DefaultMutableTreeNode folder = getFolder();
                    int childCount = model.getChildCount(folder);
                    int leadSelectionRow = selectionModel.getLeadSelectionRow();
                    node.setRow(leadSelectionRow + childCount + 1);

                    add(folder, node);
                    //MessageBus.INSTANCE.sentMessage(FavoritsPanel.Actions.POPUP_ADD_REQUEST);
                    break;

                case REMOVE:

                    break;



            }

        }
    }
    private void treeMouseLeftDoubleClicked(MouseEvent e) {
        if ( this.currentRequestNode!=null){
            final Object userObject = this.currentRequestNode.getUserObject();
            if (isRequest(userObject)) {
                MessageBusSingleton.INSTANCE.get().post(new LoadRequestFromTreeEvent((RequestNode)userObject));
            }
        }
    }

    private DefaultMutableTreeNode getFolder() {
        if (currentFolderNode==null) {
            return rootNode;
        }
        return currentFolderNode;
    }

    private void add(DefaultMutableTreeNode parent, RequestNode requestNode) {
        RequestNode node = new RequestNode(requestNode);
        nodeSet.add(node);
        DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(node);
        parent.add(newChild);
        MessageBusSingleton.INSTANCE.get().post(Events.UPDATE_UI);
    }

    private void edit(RequestNode requestNode) {
        RequestNode nodeEdit = new RequestNode(requestNode);
        if (!nodeSet.contains(nodeEdit)) {
            throw new IllegalArgumentException();
        }

        nodeSet.stream().filter(o->o.equals(requestNode)).findAny() //TODO: map
                .ifPresent(node1 ->
                    node1.update(nodeEdit)
                );
    }

    private void addFolder(String folderName) {
        DefaultMutableTreeNode folder = getFolder();
        folder.add(new DefaultMutableTreeNode(new Folder(folderName)));
        MessageBusSingleton.INSTANCE.get().post(Events.UPDATE_UI);
    }

    private void treeMouseRightClicked(MouseEvent e) {
        MessageBusSingleton.INSTANCE.get().post(new PopupShowEvent(e));
     }


    private void setSelectedNodes() {
        //tree.cancelEditing();
        TreePath path = this.selectionModel.getSelectionPath();
        if (path == null) {
            return;
        }
        DefaultMutableTreeNode selectedNode;
        selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        //  Get the selected object in the model
        Object selectedObj = selectedNode.getUserObject();
        if (selectedObj == rootNode) {
            this.currentFolderNode = null;
            this.currentRequestNode = null;
        } else {
            if (isFolder(selectedObj)) {
                this.currentFolderNode = selectedNode;
                this.currentRequestNode = null;
            } else  {
                this.currentFolderNode = (DefaultMutableTreeNode) selectedNode.getParent();
                this.currentRequestNode = selectedNode;
            }

        }

    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public PopupListener getPopupListener() {
        return popupListener;
    }

    @Subscribe
    public void onEvent(RequestSourcePanel.SaveButtonPushedEvent event) {
        RequestNode httpRequest = event.getTarget();
        edit(httpRequest);
    }



    private boolean isFolder(Object o){
        return o instanceof Folder;
    }



    private boolean isRequest(Object o){
        return o instanceof RequestNode;
    }
}
