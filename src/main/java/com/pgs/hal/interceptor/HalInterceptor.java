package com.pgs.hal.interceptor;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.pgs.hal.annotation.HalLink;
import com.pgs.hal.annotation.HalLinks;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.invoker.MethodDispatcher;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static com.pgs.hal.interceptor.PredicateUtil.classEq;
import static com.pgs.hal.interceptor.PredicateUtil.linkNameEq;

/**
 * Created by jpolitowicz on 12.09.2016.
 */
public class HalInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger log = LoggerFactory.getLogger(HalInterceptor.class);

    private static final RepresentationFactory representationFactory = new StandardRepresentationFactory();

    public HalInterceptor() {
        super(Phase.SETUP);

        //do not create JSON array for single element properties
        representationFactory.withFlag(RepresentationFactory.COALESCE_ARRAYS);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        log.info("** HAL Interceptor");

        Message inMessage = message.getExchange().getInMessage();
        Message outMessage = message.getExchange().getOutMessage();

        Response responseObject = getResponseObject(outMessage);
        if (responseObject == null || responseObject.getEntity() == null) {
            log.info("Nothing to do, response object is null");
            return;
        }

        //do not add HAL specific properties to error/redirect responses
        if (responseObject.getStatus() < 300) {
            Representation representation = getRepresentation(responseObject);
            Annotation[] annotations = getServiceMethod(inMessage).getAnnotations();

            processLinks(inMessage, representation, annotations);

            representation.withBean(responseObject.getEntity());
            Response result = Response.fromResponse(responseObject).entity(representation).build();
            MessageContentsList resList = new MessageContentsList(result);

            //override message content with HAL representation
            outMessage.setContent(List.class, resList);
        }
    }

    private void processLinks(Message inMessage, Representation representation, Annotation[] annotations) {
        Annotation annotation = Iterators.find(Iterators.forArray(annotations), classEq(HalLinks.class), null);

        if (annotation != null) {
            HalLinks halLinks = (HalLinks) annotation;

            if (halLinks.selfLink() && !Iterables.any(representation.getLinks(), linkNameEq("self"))) {
                String uri = Joiner.on("?").join(inMessage.get(Message.REQUEST_URI), inMessage.get(Message.QUERY_STRING));
                representation.withLink("self", uri);
            }

            for (HalLink halLink : halLinks.value()) {
                for (String uri : halLink.uris()) {
                    representation.withLink(halLink.name(), uri);
                }
            }
        }
    }

    private Response getResponseObject(Message message) {
        MessageContentsList objs = MessageContentsList.getContentsList(message);

        if(objs != null && objs.size() != 0) {
            Object responseObj = objs.get(0);
            if (responseObj instanceof Response) {
                return (Response) responseObj;
            }

            throw new RuntimeException("Expected entity of type [javax.ws.rs.core.Response]");
        }

        return null;
    }

    private Method getServiceMethod(Message message) {
        Message inMessage = message.getExchange().getInMessage();
        Method method = (Method) inMessage.get("org.apache.cxf.resource.method");
        if (method == null) {
            BindingOperationInfo bop = inMessage.getExchange().getBindingOperationInfo();
            if (bop != null) {
                MethodDispatcher md = (MethodDispatcher)
                        inMessage.getExchange().getService().get(MethodDispatcher.class.getName());
                method = md.getMethod(bop);
            }
        }
        return method;
    }

    private Representation getRepresentation(Response response) {
        if (response.getEntity() != null) {
            if (Representation.class.isAssignableFrom(response.getEntity().getClass())) {
                return (Representation) response.getEntity();
            }
        }

        return representationFactory.newRepresentation();
    }
}
