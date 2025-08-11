package com.example.spring_acl_test.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomObjectIdentityRetrievalStrategy implements org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy {

    private final List<ObjectIdProvider<?>> objectIdProviders;

    @Override
    public ObjectIdentity getObjectIdentity(Object domainObject) {
        return objectIdProviders.stream()
                .flatMap(provider -> provider.findIdOf(domainObject).stream())
                .map(id -> new ObjectIdentityImpl(domainObject.getClass(), id))
                .findFirst()
                .orElseThrow();
    }
}
