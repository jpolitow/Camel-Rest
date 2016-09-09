package com.pgs.validation;

import org.apache.cxf.message.Message;

/**
 * Created by jpolitowicz on 08.09.2016.
 */
public interface BeanValidator {
    void handleMessage(Message message);

    void handleReturnValue(Message message, Object bean);
}
