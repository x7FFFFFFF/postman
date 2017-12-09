package org.my.http.client;

import org.my.helpers.CommonHelper;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by paramonov on 24.08.17.
 */
public  class HttpRequest {

    public static final Pattern SPACE_PATTERN = Pattern.compile("\\s+");
    public static final String COLON = ":";
    private  final String method;
    private final String query;
    private final String httpVersion;
    private final String host;
    private final String port;
    private final String source;
    private final Map<String,String> fields = new LinkedHashMap<>();
    private final String body;
    private  final Pattern lineSepPattern;
    public HttpRequest(Object request) throws HttpRequestParseException {
        this(request.toString(), null, CommonHelper.SEPARATOR);
    }



    public HttpRequest(String request) throws HttpRequestParseException {
        this(request, null, CommonHelper.SEPARATOR);
    }

    public HttpRequest(String request, String body) throws HttpRequestParseException {
        this(request, body, CommonHelper.SEPARATOR);
    }

    public HttpRequest(String request, String body, String separator) throws HttpRequestParseException {
        lineSepPattern = Pattern.compile(separator);
        this.source = request;
        this.body =body;
       // String src = replaceLineEnd(request);

        final String[] lines = lineSepPattern.split(request);
        if (lines.length<2){
            throw new HttpRequestParseException("lines.length<2");
        }
        final String[] firstLine = SPACE_PATTERN.split(lines[0]);
        if (firstLine.length!=3){
            throw new HttpRequestParseException("firstLine.length!=3");
        }
        this.method = firstLine[0];
        this.query = firstLine[1];
        this.httpVersion = firstLine[2];


        final String[] secondLine = lines[1].split(COLON);
        switch (secondLine.length){
            case 2:
                this.host=secondLine[1].trim();
                this.port="80";
                break;
            case 3:
                this.host=secondLine[1].trim();
                this.port=secondLine[2];
                break;
            default:
                throw new HttpRequestParseException("secondLine.length = "+secondLine.length);
        }
        if (lines.length>2) {
           for(String line :Arrays.asList(lines).subList(2, lines.length)){
               parseField(line);
           }
        }
    }


    private void parseField(String line) throws HttpRequestParseException {
        final int firstColon = line.indexOf(COLON);
        if (firstColon == -1) {
            throw new HttpRequestParseException("Cant find field divider");
        }
        final String name = line.substring(0,firstColon);
        final String value =    line.substring(firstColon+1).trim();
        fields.put(name, value);
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public String getFirstFieldValue(){
        final Map.Entry<String, String> entry = getFields().entrySet().stream().findFirst().get();
        return entry.getValue();
    }

    public String getMethod() {
        return method;
    }

    public String getQuery() {
        return query;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getSource() {
        return source;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return method + ' ' +
                 query;
    }
}