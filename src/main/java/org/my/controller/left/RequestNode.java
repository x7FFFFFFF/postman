package org.my.controller.left;

import org.my.http.client.HttpRequest;

/**
 * Created on 09.12.2017.
 */
public class RequestNode extends Element  {
    private HttpRequest request;
    private int row;

    public RequestNode(HttpRequest request) {
        super((request!=null)?request.toString():"new request");
        this.request = request;
        //this.row = row;
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }
}