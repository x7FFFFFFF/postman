package org.my.view.left;

import org.my.controller.left.FavoritsPanelController;
import org.my.helpers.bus.Message;
import org.my.helpers.bus.MessageBus;
import org.my.http.client.HttpRequest;
import org.my.view.BasePanel;
import org.my.view.right.request.RequestSourcePanel;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Created by paramonov on 17.08.17.
 */
public class FavoritsPanel extends BasePanel {
    private static final String CREATE_FOLDER = "createFolder";
    private static final String CREATE_REQUEST = "createRequest";
    private static final String REMOVE = "remove";
    private JTree tree = new JTree();
    private DefaultMutableTreeNode rootNode = null;
    private DefaultMutableTreeNode currentFolderNode = null;
    private DefaultMutableTreeNode currentRequestNode = null;
    private PopupListener  popupListener  = new PopupListener();
    private JPopupMenu popup = new JPopupMenu();
    public enum Actions {
        LOAD_REQUEST_FROM_TREE,
        POPUP_ADD_REQUEST, POPUP_ADD_FOLDER
    }


    public FavoritsPanel() {
        DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("Requests");
        tree.setModel(new CustomModel(treeNode1));
        //FavoritsPanelController favoritsPanelController = new FavoritsPanelController(tree.getModel(), tree.getSelectionModel());
        //tree.addTreeSelectionListener(favoritsPanelController);
        tree.setEditable(true);

        tree.setCellRenderer(new MyTreeCellRenderer());


        //TODO:
   /*     tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                treeMouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)){
                    treeMouseRightClicked(e);
                } else if (SwingUtilities.isLeftMouseButton(e)){
                    if (e.getClickCount() == 1) {

                    } else if (e.getClickCount() == 2) {
                        treeMouseLeftDoubleClicked(e);
                    }
                }
            }

        });*/
        initPopupMenu();


        rootNode = (DefaultMutableTreeNode) this.tree.getModel().getRoot();
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(tree);
        addOnePane(jScrollPane);

        FavoritsPanelController.INSTANCE.init(tree.getModel(), tree.getSelectionModel());
        tree.addMouseListener(FavoritsPanelController.INSTANCE.getMouseListener());

        MessageBus.INSTANCE.publishListener(this::onEvent);
    }
    private static class PopupListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case CREATE_FOLDER:
                    MessageBus.INSTANCE.sentMessage(Actions.POPUP_ADD_FOLDER);
                    break;

                case   CREATE_REQUEST:
                    MessageBus.INSTANCE.sentMessage(Actions.POPUP_ADD_REQUEST);
                    break;

                case REMOVE:

                    break;



            }

        }
    }

    private static class CustomModel extends DefaultTreeModel {
        public CustomModel(TreeNode root) {
            super(root);
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {
            Object node = path.getLastPathComponent();
            if (node instanceof DefaultMutableTreeNode) {
                Object userObject = ((DefaultMutableTreeNode) node).getUserObject();
                if (userObject instanceof Element){
                    ((Element)userObject).setName((String)newValue);
                    nodeChanged((DefaultMutableTreeNode) node);
                }
            }
        }
    }


    private static class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            // decide what icons you want by examining the node
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                final Object userObject = node.getUserObject();
                if (userObject instanceof RequestNode) {
                    // your root node, since you just put a String as a user obj
                    setIcon(UIManager.getIcon("Tree.leafIcon"));
                } else if (userObject instanceof Folder) {

                    if (expanded) {
                        setIcon(UIManager.getIcon("Tree.openIcon"));
                    } else {
                        setIcon(UIManager.getIcon("Tree.closedIcon"));
                    }


                }
            }

            return this;
        }

    }



    private static class Element {
        String name;

        public Element(String name) {

            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        @Override
        public String toString()    {
            return name;
        }
    }

    private static class Folder extends Element {
        public Folder(String name) {
            super(name);
        }
    }
    public static class RequestNode extends Element  {
        private  HttpRequest request;
        private int row;

        public RequestNode(HttpRequest request) {
            super((request!=null)?request.toString():"new request");
            this.request = request;
            //this.row = row;
        }


        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public HttpRequest getRequest() {
            return request;
        }

        public void setRequest(HttpRequest request) {
            this.request = request;
        }
    }


    private void initPopupMenu() {
        {
            JMenuItem mi = new JMenuItem("Create Folder");
            mi.addActionListener(popupListener);
            mi.setActionCommand(CREATE_FOLDER);
            popup.add(mi);
        }
        {
            JMenuItem mi = new JMenuItem("Create Request");
            mi.addActionListener(popupListener);
            mi.setActionCommand(CREATE_REQUEST);
            popup.add(mi);
        }
        {
            JMenuItem mi = new JMenuItem("Remove");
            mi.addActionListener(popupListener);
            mi.setActionCommand(REMOVE);
            popup.add(mi);
        }
    }

    private void treeMouseRightClicked(MouseEvent e) {

           // if (e.isPopupTrigger()) {
                popup.show((JComponent) e.getSource(), e.getX(), e.getY());
          //  }

    }

    private boolean isFolder(Object o){
        return o instanceof Folder;
    }


    private boolean isRequest(Object o){
        return o instanceof RequestNode;
    }




    private void onEvent(Message message) {
        if (message.getId()== RequestSourcePanel.Actions.SAVE_BUTTON_PUSHED) {
            final HttpRequest request = message.getSource();
            addRequest(request);

        } else if (message.getId()==Actions.POPUP_ADD_FOLDER) {
            addFolder("New folder");
        } else if (message.getId()==Actions.POPUP_ADD_REQUEST) {
            addRequest(null);
        }
    }

    private void addRequest(HttpRequest request) {
        int leadSelectionRow = tree.getLeadSelectionRow();
        System.out.println("leadSelectionRow = " + leadSelectionRow);
        RequestNode requestNode = new RequestNode(request);
        DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(requestNode);

        if (currentFolderNode==null) {
            rootNode.add(newChild);
        } else {
            currentFolderNode.add(newChild);
        }

        tree.updateUI();
    }



    private void addFolder(String folder) {
        if (currentFolderNode==null) {
            rootNode.add(new DefaultMutableTreeNode(new Folder(folder)));
        } else {
            currentFolderNode.add(new DefaultMutableTreeNode(new Folder(folder)));
        }
        tree.updateUI();
    }
    private void treeMouseLeftDoubleClicked(MouseEvent e) {
        if ( this.currentRequestNode!=null){
            final Object userObject = this.currentRequestNode.getUserObject();
            if (isRequest(userObject)) {
                MessageBus.INSTANCE.sentMessage(Actions.LOAD_REQUEST_FROM_TREE, ((RequestNode)userObject).getRequest());
            }
        }
    }

    private void treeMouseClicked(MouseEvent evt) {
        tree.cancelEditing();
        TreePath path = tree.getSelectionPath();
        if (path == null) {
            return;
        }
        DefaultMutableTreeNode selectedNode;
        selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
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
            //  View the result
            view();
        }

    }

    private void view() {

    }
}