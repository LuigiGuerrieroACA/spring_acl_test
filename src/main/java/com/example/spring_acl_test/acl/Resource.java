package com.example.spring_acl_test.acl;

import com.example.spring_acl_test.dashboard.Dashboard;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public enum Resource {
    DASHBOARD(Dashboard.class, DashboardIdProvider.class),
    RESOURCE(Resource.class);

    private final String className;
    private final Class<? extends ObjectIdProvider<?, ?>> idProviderClass;


    Resource(Class<?> clazz, Class<? extends ObjectIdProvider<?, ?>> idProviderClass) {
        this.className = clazz.getName();
        this.idProviderClass = idProviderClass;

    }

    Resource(Class<?> clazz) {
        this.className = clazz.getName();
        this.idProviderClass = null;
    }

    public String id() {
        return name();
    }

    public String className() {
        return className;
    }

    public String idType() {
        if (idProviderClass != null) {
            Type[] genericInterfaces = idProviderClass.getGenericInterfaces();
            for (Type type : genericInterfaces) {
                if (type instanceof ParameterizedType pt) {
                    if (pt.getRawType() == ObjectIdProvider.class) {
                        Type idType = pt.getActualTypeArguments()[1];
                        if (idType instanceof Class) {
                            return ((Class<?>) idType).getName();
                        }
                    }
                }
            }
            return Object.class.getName();
        }
        return String.class.getName();
    }

}

