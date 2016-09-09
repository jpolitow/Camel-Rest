package com.pgs.validation;

import org.apache.camel.util.CastUtils;
import org.apache.cxf.message.Message;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by jpolitowicz on 08.09.2016.
 */
@Service("beanValidator")
public class BeanValidatorImpl extends AbstractValidator implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void handleMessage(Message message) {
        final Method method = getServiceMethod(message);
        if (method == null) {
            return;
        }

        final Object theServiceObject = applicationContext.getBean(method.getDeclaringClass());
        final List<Object> arguments = CastUtils.cast(message.getContent(List.class));

        if (arguments.size() > 0) {
            BeanValidationProvider provider = getProvider();
            provider.validateParameters(theServiceObject, method, arguments.toArray());
        }
    }

    @Override
    public void handleReturnValue(Message message, Object resultBean) {
        final Method method = getServiceMethod(message);
        if (method == null) {
            return;
        }

        Class<?> returnType = method.getReturnType();
        BeanValidationProvider provider = getProvider();

        if (returnType.isAssignableFrom(Response.class)) {
            Response response = (Response) resultBean;
            provider.validateReturnValue(response.getEntity());
        } else {
            provider.validateReturnValue(resultBean);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
