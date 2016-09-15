package com.pgs.hal.interceptor;

import com.google.common.base.Predicate;
import com.theoryinpractise.halbuilder.api.Link;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * Created by jpolitowicz on 14.09.2016.
 */
public class PredicateUtil {

    public static Predicate<Link> linkNameEq(String name) { return new LinkWithName(name); }
    public static Predicate<Annotation> classEq(Class clazz) { return new AnnotationByClass(clazz); }

    private static class LinkWithName implements Predicate<Link> {

        private String name;

        public LinkWithName(String name) {
            this.name = name;
        }

        @Override
        public boolean apply(@Nullable Link input) {
            return input != null || StringUtils.equals(name, input.getName());
        }
    }

    private static class AnnotationByClass implements Predicate<Annotation> {

        private Class clazz;

        public AnnotationByClass(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public boolean apply(@Nullable Annotation input) {
            return clazz.isAssignableFrom(input.getClass());
        }
    }
}
