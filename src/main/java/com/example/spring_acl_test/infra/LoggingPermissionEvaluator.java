package com.example.spring_acl_test.infra;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class LoggingPermissionEvaluator implements PermissionEvaluator {
    private final PermissionEvaluator delegate;

    public LoggingPermissionEvaluator(PermissionEvaluator delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        System.out.println("Checking permission " + permission + " for " + authentication.getName() + " on id " + targetId + " type " + targetType);
        boolean result = delegate.hasPermission(authentication, targetId, targetType, permission);
        System.out.println("Permission result: " + result);
        return result;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        System.out.println("Checking permission " + permission + " for " + authentication.getName() + " on " + targetDomainObject);
        boolean result = delegate.hasPermission(authentication, targetDomainObject, permission);
        System.out.println("Permission result: " + result);
        return result;
    }
}

