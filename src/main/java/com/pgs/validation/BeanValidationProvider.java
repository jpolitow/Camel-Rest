package com.pgs.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.*;
import javax.validation.executable.ExecutableValidator;
import javax.validation.spi.ValidationProvider;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class BeanValidationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(BeanValidationProvider.class);

    private final ValidatorFactory factory;

    public BeanValidationProvider() {
        try {
            factory = Validation.buildDefaultValidatorFactory();
        } catch (final ValidationException ex) {
            LOG.error("Bean Validation provider can not be found, no validation will be performed");
            throw ex;
        }
    }

    public BeanValidationProvider(ValidatorFactory factory) {
        if (factory == null) {
            throw new NullPointerException("Factory is null");
        }
        this.factory = factory;
    }

    public BeanValidationProvider(ValidationProviderResolver resolver) {
        this(resolver, null);
    }

    public <T extends Configuration<T>, U extends ValidationProvider<T>> BeanValidationProvider(
            ValidationProviderResolver resolver,
            Class<U> providerType) {
        this(resolver, providerType, null);
    }

    public <T extends Configuration<T>, U extends ValidationProvider<T>> BeanValidationProvider(
            ValidationProviderResolver resolver,
            Class<U> providerType,
            ValidationConfiguration cfg) {
        try {
            Configuration<?> factoryCfg = providerType != null
                    ? Validation.byProvider(providerType).providerResolver(resolver).configure()
                    : Validation.byDefaultProvider().providerResolver(resolver).configure();
            initFactoryConfig(factoryCfg, cfg);
            factory = factoryCfg.buildValidatorFactory();
        } catch (final ValidationException ex) {
            LOG.error("Bean Validation provider can not be found, no validation will be performed");
            throw ex;
        }
    }

    private static void initFactoryConfig(Configuration<?> factoryCfg, ValidationConfiguration cfg) {
        if (cfg != null) {
            factoryCfg.parameterNameProvider(cfg.getParameterNameProvider());
            factoryCfg.messageInterpolator(cfg.getMessageInterpolator());
            factoryCfg.traversableResolver(cfg.getTraversableResolver());
            factoryCfg.constraintValidatorFactory(cfg.getConstraintValidatorFactory());
            for (Map.Entry<String, String> entry : cfg.getProperties().entrySet()) {
                factoryCfg.addProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    public <T> void validateParameters(final T instance, final Method method, final Object[] arguments) {
        final ExecutableValidator methodValidator = getExecutableValidator();
        final Set<ConstraintViolation<T>> violations = methodValidator.validateParameters(instance,
                method, arguments);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public <T> void validateReturnValue(final T instance, final Method method, final Object returnValue) {
        final ExecutableValidator methodValidator = getExecutableValidator();
        final Set<ConstraintViolation<T>> violations = methodValidator.validateReturnValue(instance,
                method, returnValue);

        if (!violations.isEmpty()) {
            throw new ResponseConstraintViolationException(violations);
        }
    }

    public <T> void validateReturnValue(final T bean) {
        final Set<ConstraintViolation<T>> violations = doValidateBean(bean);
        if (!violations.isEmpty()) {
            throw new ResponseConstraintViolationException(violations);
        }
    }

    public <T> void validateBean(final T bean) {
        final Set<ConstraintViolation<T>> violations = doValidateBean(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private <T> Set<ConstraintViolation<T>> doValidateBean(final T bean) {
        return factory.getValidator().validate(bean);
    }

    private ExecutableValidator getExecutableValidator() {
        return factory.getValidator().forExecutables();
    }
}
