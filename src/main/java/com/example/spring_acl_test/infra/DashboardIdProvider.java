package com.example.spring_acl_test.infra;

import com.example.spring_acl_test.dashboard.Dashboard;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class DashboardIdProvider  implements ObjectIdProvider<Dashboard> {

    @Override
    public Serializable idOf(Dashboard object) {
        return object.getId();
    }

}
