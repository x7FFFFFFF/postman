package org.my.http.client;

import org.my.helpers.CommonHelper;

/**
 * Created on 18.11.2017.
 */
public class RequestMaker {
    private  final String separator;
    private final  StringBuilder request = new StringBuilder();

    public RequestMaker(RequestMaker other) {
        this.separator = other.getSeparator();
        request.append(other.getRequest());
    }
    public RequestMaker(RequestMaker other, String separator) {

        if (!other.getSeparator().equals(separator)) {
            request.append(other.getRequest().toString().replace(other.getSeparator(), separator));
        } else{
            request.append(other.getRequest());
        }
        this.separator = separator;
    }
    public RequestMaker() {
        this.separator = CommonHelper.SEPARATOR;
    }


    public RequestMaker(String separator) {
        this.separator = separator;
    }
    public RequestMaker line(String line){
        request.append(line);
        request.append(separator);
        return this;
    }

    public String getSeparator() {
        return separator;
    }

    public StringBuilder getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return request.toString();
    }
}
