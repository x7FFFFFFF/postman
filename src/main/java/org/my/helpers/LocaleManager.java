package org.my.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by paramonov on 23.08.17.
 */
public final class LocaleManager {
    public static  final Locale RU_LOCALE = new Locale("ru", "RU", "");
    public static  final Locale EN_LOCALE = new Locale("en", "US", "");
    private static final LocaleManager INSTANCE= new LocaleManager();
    private volatile Locale currentLocale;
    private volatile ResourceBundle bundle;
    private volatile Map<String,ResourceBundle> map = new HashMap<>();



    private volatile String defaultBundleName;

    public  void loadBundle(String name){
        bundle= ResourceBundle.getBundle(name, new UTF8Control());
        map.put(name, bundle);
    }
    public String translate(String bundleName, String keyName){
        final ResourceBundle bundle = getBundle(bundleName);
        return bundle.getString(keyName);
    }
    public String translate(String keyName){
        final ResourceBundle bundle = getBundle(getDefaultBundleName());
        return bundle.getString(keyName);
    }


    public ResourceBundle getBundle(String name){
        return map.get(name);
    }

    public void reloadBundles(){
        map.replaceAll((k,v)->v=ResourceBundle.getBundle(k, getCurrentLocale()));
    }

    private LocaleManager() {
        setCurrentLocale(EN_LOCALE);
    }

    public static LocaleManager get(){
        return INSTANCE;
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
        reloadBundles();
    }

    public String getDefaultBundleName() {
        return defaultBundleName;
    }

    public void setDefaultBundleName(String defaultBundleName) {
        this.defaultBundleName = defaultBundleName;
    }

    public class UTF8Control extends ResourceBundle.Control {
        public ResourceBundle newBundle
                (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException
        {
            // The below is a copy of the default implementation.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // Only this line is changed to make it to read properties files as UTF-8.
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}