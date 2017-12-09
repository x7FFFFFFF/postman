package org.my.http.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by paramonov on 24.08.17.
 */
public class SimpleHttpClient implements IHttpClient {


    @Override
    public HttpResponse doRequest(HttpRequest parser) throws Exception {
        URL  url = new URL("http://"+parser.getHost()+":"+parser.getPort()+parser.getQuery());
        final HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod(parser.getMethod());
        urlConnection.setUseCaches( false );
        parser.getFields().forEach(urlConnection::setRequestProperty);
        switch (parser.getMethod()){
            case "POST":
            case "PUT":
               if  (parser.getBody()!=null){
                   byte[] postData       = parser.getBody().getBytes( StandardCharsets.UTF_8 );
                   int    postDataLength = postData.length;
                   urlConnection.setDoOutput( true );
                   urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                  try( final OutputStream outputStream = urlConnection.getOutputStream()) {
                      outputStream.write(postData);
                      outputStream.flush();
                  }
                   urlConnection.setRequestProperty("Content-Length", String.valueOf(postData.length));
               }
        }



        String res;
        //urlConnection.setInstanceFollowRedirects( false );
        try {
            res = fromStream(getStream(urlConnection));
        } catch (Throwable t){
            return new HttpResponse(urlConnection.getResponseCode(), urlConnection.getHeaderFields(), getError(urlConnection));
        }
        return new HttpResponse(urlConnection.getResponseCode(), urlConnection.getHeaderFields(), res);

    }

    private String fromStream(InputStream in) throws IOException {
        String res = "";
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            while ((line = reader.readLine()) != null) {
                res += line;
            }
        }
        return res;
    }

    private String getError(HttpURLConnection urlConnection) {
        String res = "";
        try {
            res = fromStream(getErrorStream(urlConnection));
        } catch (Throwable t){
            //TODO: handle
        }
        return res;
    }

    private InputStream getStream(HttpURLConnection urlConnection) throws IOException {
        if (isGzip(urlConnection)){
            return new GZIPInputStream(urlConnection.getInputStream());
        }
        return  urlConnection.getInputStream();
    }
    private InputStream getErrorStream(HttpURLConnection urlConnection) throws IOException {
        if (isGzip(urlConnection)){
            return new GZIPInputStream(urlConnection.getErrorStream());
        }
        return  urlConnection.getErrorStream();
    }



    private boolean isGzip(HttpURLConnection urlConnection) {
        final List<String> list = urlConnection.getHeaderFields().get("Content-Encoding");
        return list != null && list.stream().anyMatch(s -> s.equalsIgnoreCase("gzip"));
    }
}