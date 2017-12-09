package org.my.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.my.http.client.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by paramonov on 28.08.17.
 */
public  class TestHandler implements HttpHandler {
    private final HttpRequest request;
    private final String responseBody;
    private final int responseStatus;

    public TestHandler(HttpRequest request, String response, int responseStatus) {
        this.request = request;
        this.responseBody = response;
        this.responseStatus = responseStatus;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
          /*  StringBuilder builder = new StringBuilder();

            builder.append("<h1>URI: ").append(exchange.getRequestURI()).append("</h1>");

            Headers headers = exchange.getRequestHeaders();
            for (String header : headers.keySet()) {
                builder.append("<p>").append(header).append("=")
                        .append(headers.getFirst(header)).append("</p>");
            }*/

        byte[] bytes = responseBody.getBytes();

        exchange.sendResponseHeaders(responseStatus, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}