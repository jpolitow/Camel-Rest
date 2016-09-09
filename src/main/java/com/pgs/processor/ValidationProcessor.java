package com.pgs.processor;

import com.google.common.collect.Iterables;
import com.pgs.model.ErrorResponse;
import com.pgs.validation.BeanValidator;
import com.pgs.validation.ResponseConstraintViolationException;
import org.apache.camel.Exchange;
import org.apache.cxf.jaxrs.impl.UriBuilderImpl;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

/**
 * Created by jpolitowicz on 07.09.2016.
 */
@Service("validationProcessor")
public class ValidationProcessor {

    @Resource(name = "beanValidator")
    private BeanValidator beanValidator;

    public void validateInput(Exchange exchange) {
        if (exchange != null) {
            if (exchange.getIn() != null) {
                if (exchange.getIn().getHeaders().containsKey("CamelCxfMessage")) {
                    beanValidator.handleMessage((Message) exchange.getIn().getHeaders().get("CamelCxfMessage"));
                }
            }
        }
    }

    public void validateBean(Exchange exchange) {
        if (exchange != null) {
            if (exchange.getIn() != null) {
                if (exchange.getIn().getHeaders().containsKey("CamelCxfMessage")) {
                    beanValidator.handleReturnValue((Message) exchange.getIn().getHeaders().get("CamelCxfMessage"), exchange.getIn().getBody());
                }
            }
        }
    }

    public Response handleConstraintViolationException(Exchange exchange) {
        ErrorResponse error = new ErrorResponse();
        Throwable exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);

        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;

            error.setType("http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html");
            error.setTitle("Request Validation Failure");
            error.setStatus(Response.Status.BAD_REQUEST.getStatusCode());

            ConstraintViolation<?> constraintViolation = Iterables.getFirst(constraintViolationException.getConstraintViolations(), null);
            error.setDetail(constraintViolation.getMessage());

            fillErrorUri(exchange, error);
        }

        return Response.status(error.getStatus()).entity(error).build();
    }

    public Response handleResponseConstraintViolationException(Exchange exchange) {
        ErrorResponse error = new ErrorResponse();
        Throwable exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);

        if (exception instanceof ResponseConstraintViolationException) {
            ResponseConstraintViolationException validationException = (ResponseConstraintViolationException) exception;

            error.setType("http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html");
            error.setTitle("Response Validation Failure");
            error.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

            ConstraintViolation<?> constraintViolation = Iterables.getFirst(validationException.getConstraintViolations(), null);
            error.setDetail(constraintViolation.getMessage());

            fillErrorUri(exchange, error);
        }

        return Response.status(error.getStatus()).entity(error).build();
    }

    private void fillErrorUri(Exchange exchange, ErrorResponse error) {
        if (exchange.getIn().getHeaders().containsKey("CamelCxfMessage")) {
            Map<String, Object> cxfMessages = (Map<String, Object>) exchange.getIn().getHeaders().get("CamelCxfMessage");

            UriBuilder uriBuilder = new UriBuilderImpl(URI.create((String) cxfMessages.get("org.apache.cxf.request.uri")));
            uriBuilder.path("error").path(exchange.getExchangeId());

            error.setInstance(uriBuilder.toTemplate());
        }
    }
}
