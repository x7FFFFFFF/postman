package org.my.http.client;

import org.junit.Assert;
import org.junit.Test;
import org.my.helpers.CommonHelper;

/**
 * Created by paramonov on 24.08.17.
 */
public class HttpRequestTest {


    public   static final RequestMaker MIN_REQUEST = new RequestMaker(CommonHelper.SEPARATOR)
        .line("GET /vip-olap/getModifiedBookings?dateFrom=2017-08-23 HTTP/1.1 ")
        .line("Host: 127.0.5.42:9090");



    @Test
    public void testReqPaserReferer() throws HttpRequestParseException {
        RequestMaker requestWithRef = new RequestMaker(MIN_REQUEST)
                .line("Referer: http://camel.apache.org/tutorial-example-reportincident-part4.html");
        HttpRequest parser = new HttpRequest(requestWithRef);
        Assert.assertEquals("http://camel.apache.org/tutorial-example-reportincident-part4.html", parser.getFields().get("Referer"));
    }



    @Test
    public void testReqPaser() throws HttpRequestParseException {
        {
            String separator = "\n";
            RequestMaker requestLF = new RequestMaker(MIN_REQUEST, separator)
                    .line("Connection: keep-alive")
                    .line("Pragma: no-cache")
                    .line( "Cache-Control: no-cache");

            HttpRequest parser = new HttpRequest(requestLF.toString(), null, separator);

            Assert.assertEquals("GET", parser.getMethod());
            Assert.assertEquals("/vip-olap/getModifiedBookings?dateFrom=2017-08-23", parser.getQuery());
            Assert.assertEquals("HTTP/1.1", parser.getHttpVersion());
            Assert.assertEquals("127.0.5.42", parser.getHost());
            Assert.assertEquals("9090", parser.getPort());
            Assert.assertEquals("no-cache", parser.getFields().get("Cache-Control"));
        }

   }


}