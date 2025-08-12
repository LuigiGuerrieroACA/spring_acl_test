package com.example.spring_acl_test.acl;

public enum Resource {
    DASHBOARD();

    public String id() {
        return name();
    }
}

