package com.example.spring_acl_test.acl;

import com.example.spring_acl_test.dashboard.Dashboard;
import org.springframework.stereotype.Component;


@Component
public class DashboardIdProvider implements ObjectIdProvider<Dashboard, String> {

    @Override
    public String idOf(Dashboard object) {
        return object.getId();
    }

}
