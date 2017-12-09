package org.my.http.client;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.my.server.TestHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by paramonov on 24.08.17.
 */
public class SimpleHttpClientTest {
    ExecutorService pool = Executors.newFixedThreadPool(1, r -> {
                Thread thread = new Thread(r, "Test server");
                return thread;
            }
    );

    @Before
    public void init() throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(9090), 0);
        TestHandler handler = new TestHandler(null, "", 400);

        HttpContext context = server.createContext("/", handler);

        server.setExecutor(pool);
        server.start();
    }


    @Test
    public void testMethodDetection() throws Exception {

        HttpRequest request = new HttpRequest(HttpRequestTest.MIN_REQUEST);


        SimpleHttpClient client = new SimpleHttpClient();
        HttpResponse response = client.doRequest(request);




        Assert.assertEquals(400,response.getStatus());

    }
}