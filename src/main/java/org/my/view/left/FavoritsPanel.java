package org.my.view.left;

import org.my.bus.MessageBusSingleton;
import org.my.bus.Subscribe;
import org.my.controller.left.Element;
import org.my.controller.left.FavoritsPanelController;
import org.my.controller.left.Folder;
import org.my.controller.left.RequestNode;

import org.my.view.BasePanel;

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
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Created by paramonov on 17.08.17.
 */
public class FavoritsPanel extends BasePanel  {

    private JTree tree = new JTree();
    private DefaultMutableTreeNode rootNode;
    private DefaultMutableTreeNode currentFolderNode;
    private DefaultMutableTreeNode currentRequestNode;

    private JPopupMenu popup = new JPopupMenu();


    public FavoritsPanel() {
        DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("Requests");
        tree.setModel(new CustomModel(treeNode1));

        tree.setEditable(true);

        tree.setCellRenderer(new MyTreeCellRenderer());


        rootNode = (DefaultMutableTreeNode) this.tree.getModel().getRoot();
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(tree);
        addOnePane(jScrollPane);

        FavoritsPanelController favoritsPanelController = FavoritsPanelController.INSTANCE;
        MessageBusSingleton.INSTANCE.get().register(favoritsPanelController);
        favoritsPanelController.init(tree.getModel(), tree.getSelectionModel());
        tree.addMouseListener(favoritsPanelController.getMouseListener());
        initPopupMenu(favoritsPanelController.getPopupListener());

    }


    private static class CustomModel extends DefaultTreeModel {
        CustomModel(TreeNode root) {
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









    private void initPopupMenu(ActionListener popupListener) {
        {
            JMenuItem mi = new JMenuItem("Create Folder");
            mi.addActionListener(popupListener);
            mi.setActionCommand(FavoritsPanelController.CREATE_FOLDER);
            popup.add(mi);
        }
        {
            JMenuItem mi = new JMenuItem("Create Request");
            mi.addActionListener(popupListener);
            mi.setActionCommand(FavoritsPanelController.CREATE_REQUEST);
            popup.add(mi);
        }
        {
            JMenuItem mi = new JMenuItem("Remove");
            mi.addActionListener(popupListener);
            mi.setActionCommand(FavoritsPanelController.REMOVE);
            popup.add(mi);
        }
    }

    @Subscribe
    public void onEventShowPopup(FavoritsPanelController.PopupShowEvent message) {
        MouseEvent e = message.getTarget();
        popup.show((JComponent) e.getSource(), e.getX(), e.getY());
    }


    @Subscribe
    public void onEventUpdate(FavoritsPanelController.Events event) {
      if (event == FavoritsPanelController.Events.UPDATE_UI){
           tree.updateUI();
        }
    }







}