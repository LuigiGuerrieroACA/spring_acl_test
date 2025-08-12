package com.example.spring_acl_test.acl;

import org.springframework.core.ResolvableType;

import java.io.Serializable;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.springframework.core.GenericTypeResolver.resolveTypeArgument;

public interface ObjectIdProvider<T, ID extends Serializable> {

    default boolean supports(Class<?> type)
    {
        var supportedType = requireNonNull(resolveFirstGenericType(getClass()));
        return supportedType.isAssignableFrom(type);
    }

    ID idOf(T object);

    @SuppressWarnings("unchecked")
    default Optional<Serializable> findIdOf(Object object)
    {
        return supports(object.getClass())
                ? Optional.of(idOf((T) object))
                : Optional.empty();
    }

    static Class<?> resolveFirstGenericType(Class<?> clazz) {
        ResolvableType resolvableType = ResolvableType.forClass(clazz)
                .as(ObjectIdProvider.class);
        if (resolvableType.hasGenerics()) {
            Class<?> firstType = resolvableType.getGeneric(0).resolve();
            return firstType != null ? firstType : Object.class;
        }
        return null;
    }

}
