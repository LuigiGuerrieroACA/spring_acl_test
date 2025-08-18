package com.example.spring_acl_test.user;

import java.util.Map;

public record AccessControlRequest(Map<String, ResourceAccess> accessControl) {
}
