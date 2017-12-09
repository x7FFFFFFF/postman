package org.my.helpers;


import javax.swing.*;

import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.stream.Stream;

/**
 * Created by paramonov on 17.08.17.
 */
public class UiHelper {

    public static void flattenSplitPane(JSplitPane withCustomDivider) {
      /*  jSplitPane.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {
                    }
                };
            }
        });
        jSplitPane.setBorder(null);*/

        BasicSplitPaneDivider divider = ((BasicSplitPaneUI) withCustomDivider.getUI()).getDivider();
        withCustomDivider.setOneTouchExpandable(true);
        divider.setDividerSize(7);

        divider.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

    }


    public static Component getChildNamed(Component parent, String name) {

        // Debug line
        //System.out.println("Class: " + parent.getClass() +
        //    " Name: " + parent.getName());

        if (name.equals(parent.getName())) {
            return parent;
        }

        if (parent instanceof Container) {
            Component[] children = ((Container) parent).getComponents();

            for (int i = 0; i < children.length; ++i) {
                Component child = getChildNamed(children[i], name);
                if (child != null) {
                    return child;
                }
            }
        }

        return null;
    }

    public static Component getFirstChildByName(Component parent, String name) {


        if (parent instanceof Container) {
            Component[] children = ((Container) parent).getComponents();

            for (int i = 0; i < children.length; ++i) {
                if (name.equals(children[i].getName())) {
                    return children[i];
                }
            }
        }

        return null;
    }

   public static JSplitPane createSplitPane(int orientation,  Component newLeftComponent,
                               Component newRightComponent) {
    JSplitPane splitPane = new JSplitPane(orientation,
            newLeftComponent, newRightComponent);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
       splitPane.setBorder(null);
        UiHelper.flattenSplitPane(splitPane);
        return splitPane;
    }

    public  static  void  setLookAndFill(String name){
        Stream.of(javax.swing.UIManager.getInstalledLookAndFeels())
                .filter(info->info.getName().equalsIgnoreCase(name)).findAny().ifPresent(
                        info->{
                            try {
                                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
        );
    }


    public  static  String getText(Document document)  {
        String res = "";
        try {
            res = document.getText(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return  res;
    }

}