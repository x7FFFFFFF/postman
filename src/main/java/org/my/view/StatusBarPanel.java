package org.my.view;

import org.my.Main;
import org.my.helpers.UiHelper;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by paramonov on 04.09.17.
 */
public class StatusBarPanel extends BasePanel {
    private JLabel statusLabel;
    private static final  String OK_TEXT = "status: OK";
    public StatusBarPanel() {
        setBorder(new BevelBorder(BevelBorder.LOWERED));

        statusLabel = new JLabel(OK_TEXT);
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        setMinimumSize(new Dimension(200, 16));
        add(statusLabel);

    }
    public static void setStatusBarErr(String txt){
        StatusBarPanel  statusBarPanel  = (StatusBarPanel) UiHelper.getChildNamed(Main.mainFrame.getContentPane(), StatusBarPanel.class.getCanonicalName());
        statusBarPanel.setError(txt);
    }

    public static void setStatusBarMsg(String txt){
        StatusBarPanel  statusBarPanel  = (StatusBarPanel) UiHelper.getChildNamed(Main.mainFrame.getContentPane(), StatusBarPanel.class.getCanonicalName());
        statusBarPanel.setOk(txt);
    }

    public void setOk(String txt){
        statusLabel.setText(txt);
        statusLabel.setForeground(Color.green);
    }

    public void setError(String txt){
        statusLabel.setText(txt);
        statusLabel.setForeground(Color.red);
    }


    @Override
    protected void setDefaultLayout() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }
}