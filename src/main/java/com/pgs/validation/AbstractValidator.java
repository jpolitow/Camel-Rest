package com.pgs.validation;

import org.apache.cxf.message.Message;
import org.apache.cxf.service.invoker.MethodDispatcher;
import org.apache.cxf.service.model.BindingOperationInfo;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by jpolitowicz on 08.09.2016.
 */
public abstract class AbstractValidator implements BeanValidator {

    private volatile BeanValidationProvider provider;

    protected Method getServiceMethod(Message message) {
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

    protected BeanValidationProvider getProvider() {
        if (provider == null) {
            provider = new BeanValidationProvider();
        }

        return provider;
    }

}
