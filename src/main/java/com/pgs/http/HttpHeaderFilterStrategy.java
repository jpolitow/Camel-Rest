package com.pgs.http;

import org.apache.camel.impl.DefaultHeaderFilterStrategy;

/**
 * Created by jpolitowicz on 09.09.2016.
 */
public class HttpHeaderFilterStrategy extends DefaultHeaderFilterStrategy {

    public HttpHeaderFilterStrategy() {
        initialize();
    }

    protected void initialize() {
        getOutFilter().add("breadcrumbid");

        getOutFilter().add("content-length");
        getOutFilter().add("content-type");
        // Add the filter for the Generic Message header
        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.5
        getOutFilter().add("cache-control");
        getOutFilter().add("connection");
        getOutFilter().add("date");
        getOutFilter().add("pragma");
        getOutFilter().add("trailer");
        getOutFilter().add("transfer-encoding");
        getOutFilter().add("upgrade");
        getOutFilter().add("via");
        getOutFilter().add("warning");

        setLowerCase(true);

        // filter headers begin with "Camel" or "org.apache.camel"
        // must ignore case for Http based transports
        setOutFilterPattern("(?i)(Camel|org\\.apache\\.camel)[\\.|a-z|A-z|0-9]*");
    }
}
