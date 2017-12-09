package org.my;

import org.my.helpers.LocaleManager;
import org.my.helpers.UiHelper;
import org.my.view.MainFrame;

/**
 * Created by paramonov on 17.08.17.
 */
public class Main {

    public static MainFrame mainFrame;
    public static void main(String[] args) {
        UiHelper.setLookAndFill("Windows");
        LocaleManager.get().setCurrentLocale(LocaleManager.RU_LOCALE);
        LocaleManager.get().loadBundle("org.my.l18n.transl");
        LocaleManager.get().setDefaultBundleName("org.my.l18n.transl");

        mainFrame =  MainFrame.getInstance();
        //publish listeners


       // mainFrame.pack();
        mainFrame.setVisible(true);
    }
}