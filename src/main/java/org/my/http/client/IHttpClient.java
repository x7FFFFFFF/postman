package org.my.http.client;

/**
 * Created by paramonov on 24.08.17.
 */
public interface IHttpClient {



    HttpResponse  doRequest(HttpRequest parser) throws Exception;


}