package com.example.spring_acl_test.infra;

import java.io.Serializable;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.springframework.core.GenericTypeResolver.resolveTypeArgument;

public interface ObjectIdProvider<T> {

    default boolean supports(Class<?> type)
    {
        var supportedType = requireNonNull(resolveTypeArgument(getClass(), ObjectIdProvider.class));
        return supportedType.isAssignableFrom(type);
    }

    Serializable idOf(T object);

    @SuppressWarnings("unchecked")
    default Optional<Serializable> findIdOf(Object object)
    {
        return supports(object.getClass())
                ? Optional.of(idOf((T) object))
                : Optional.empty();
    }

}
