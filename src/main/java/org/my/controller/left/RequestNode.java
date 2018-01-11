package org.my.controller.left;



/**
 * Created on 09.12.2017.
 */
public class RequestNode extends Element  {
    private String requestHeader;
    private String requestBody;
    private int row;

    public  static RequestNode createNewEmpty(){
        return new RequestNode("New Request", "", "", 0);
    }

    public RequestNode(RequestNode node) {
        super(node.getName());
        this.requestHeader = node.getRequestHeader();
        this.requestBody = node.getRequestBody();
        this.row = node.getRow();
    }

    public RequestNode(String name, String requestHeader, String requestBody, int row) {
        super(name);
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
        this.row = row;
    }
    public void update(RequestNode node){
        this.requestHeader = node.getRequestHeader();
        this.requestBody = node.getRequestBody();
        this.row = node.getRow();
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestNode that = (RequestNode) o;

        return row == that.row;
    }

    @Override
    public int hashCode() {
        return row;
    }
}