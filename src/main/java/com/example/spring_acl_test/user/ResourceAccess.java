package com.example.spring_acl_test.user;

import jakarta.annotation.Nullable;

import java.util.List;

public record ResourceAccess(boolean allowAll, @Nullable List<String> ids) {
}
