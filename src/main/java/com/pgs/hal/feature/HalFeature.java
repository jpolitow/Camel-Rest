package com.pgs.hal.feature;

import com.pgs.hal.interceptor.HalInterceptor;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

public class HalFeature extends AbstractFeature {

    @Override
    protected void initializeProvider(InterceptorProvider provider, Bus bus) {
        provider.getOutInterceptors().add(new HalInterceptor());
    }
}
