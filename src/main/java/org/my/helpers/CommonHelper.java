package org.my.helpers;

import java.util.regex.Pattern;

/**
 * Created on 18.11.2017.
 */
public class CommonHelper {
    public static final String SEPARATOR = System.getProperty("line.separator");
    public static  final Pattern LINE_SEP_PATTERN = Pattern.compile(SEPARATOR);
    public static final String RESPONSE_EVENT = "response";
}
