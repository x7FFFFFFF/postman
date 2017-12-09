package org.my.collapse;

import org.my.AccordianTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by paramonov on 14.09.17.
 */
public class Test {

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CPanel p = new CPanel();
        p.setMoveSpeed(10);
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.add(new JLabel("header"), BorderLayout.CENTER);
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(new JLabel("content"),  BorderLayout.CENTER);
        p.setHeaderPane(header);
        p.setContentPane(content);
        p.setMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
               // p.collapse(!p.isCollapsed());
                p.moveRelativeY(100);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        p.draw();

        f.getContentPane().add(p);
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}

class CPanel extends AbstractChildPanel{

}