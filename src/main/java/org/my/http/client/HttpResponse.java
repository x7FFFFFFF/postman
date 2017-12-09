package org.my.http.client;

import java.util.List;
import java.util.Map;

/**
 * Created by paramonov on 28.08.17.
 */
public class HttpResponse {
    private final int status;
    private final  Map<String,List<String>> headers;
    private final  String body;
    private final static  String LINE_SEP = System.getProperty("line.separator");

    public HttpResponse(int status,  Map<String,List<String>> headers, String body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }


    public int getStatus() {
        return status;
    }

    public  Map<String,List<String>> getHeaders() {
        return headers;
    }

    public String getHeadersStr(){
        StringBuilder sb = new StringBuilder();
        headers.forEach(
                (k,v)->{
                    sb.append(k).append(": ").append(v).append(LINE_SEP);
                }
        );
        return sb.toString();
    }

    public String getBody() {
        return body;
    }
}