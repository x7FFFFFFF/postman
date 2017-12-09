package org.my.http.client;

/**
 * Created by paramonov on 24.08.17.
 */
public class HttpRequestParseException extends Exception{
    public HttpRequestParseException() {
    }

    public HttpRequestParseException(String message) {
        super(message);
    }

    public HttpRequestParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestParseException(Throwable cause) {
        super(cause);
    }

    public HttpRequestParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}