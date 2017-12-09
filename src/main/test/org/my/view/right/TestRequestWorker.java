package org.my.view.right;

import org.my.http.client.HttpRequest;
import org.my.http.client.HttpResponse;

import javax.swing.*;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Created by paramonov on 23.08.17.
 */
public class TestRequestWorker extends SwingWorker<HttpResponse, Void> {
    private final HttpRequest request;
    public TestRequestWorker(HttpRequest request) {


            this.request = request;
    }

    @Override
    protected HttpResponse doInBackground() throws Exception {
            sleepAWhile();
            HttpResponse resp = new HttpResponse(200, Collections.emptyMap(), request.getBody()+" + Response");
            return resp;
    }

    private void sleepAWhile() {
        try {
            Thread.yield();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
    }
}