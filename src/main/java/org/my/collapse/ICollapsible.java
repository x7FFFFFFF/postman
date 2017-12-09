package org.my.collapse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Created by paramonov on 14.09.17.
 */
public interface ICollapsible {

    void draw();
    void collapse(boolean collapse);
    boolean isCollapsed();
    int getId();
    void setId(int id);
    void setMouseListener(MouseListener ml);
    void setHeaderPane(JPanel pane);
    void setContentPane(JPanel pane);
    void moveRelativeY(int dY);
    void setMoveSpeed(int speed);
}