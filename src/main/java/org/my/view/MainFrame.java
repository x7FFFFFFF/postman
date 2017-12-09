package org.my.view;

import org.my.helpers.UiHelper;
import org.my.view.left.LeftPanel;
import org.my.view.right.RightPanel;

import javax.swing.*;
import java.awt.*;

import static org.my.helpers.UiHelper.createSplitPane;

/**
 * Created by paramonov on 17.08.17.
 */
public final class MainFrame extends JFrame {
    private final LeftPanel leftPane = new LeftPanel();
    private final RightPanel rightPane = new RightPanel();
    private Component currentContent;

    public LeftPanel getLeftPane() {
        return leftPane;
    }

    public RightPanel getRightPane() {
        return rightPane;
    }

    private static final class Holder {
        private static final MainFrame INSTANCE = new MainFrame();
    }
    public static MainFrame getInstance() {
        return Holder.INSTANCE;
    }

    private MainFrame() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JSplitPane splitPane =   createSplitPane(JSplitPane.HORIZONTAL_SPLIT,  leftPane, rightPane);

        getContentPane().add(new StatusBarPanel(), BorderLayout.SOUTH);
        getContentPane().add(splitPane,  BorderLayout.CENTER);
        //setContent(splitPane);


        setSize(605, 660);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Postman");
        setLocationRelativeTo(null);
    }

 /*   public void setContent(Component component) {
        Container contentPane = getContentPane();
        if (currentContent != null) {
            contentPane.remove(currentContent);
        }
        contentPane.add(component, BorderLayout.CENTER);
        currentContent = component;
        contentPane.doLayout();
        repaint();
    }*/


}