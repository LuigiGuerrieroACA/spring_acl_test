package com.example.spring_acl_test.user;

import com.example.spring_acl_test.acl.ACLService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AccessControlService {

    private final ACLService aclService;

    public AccessControlService(ACLService aclService) {
        this.aclService = aclService;
    }

    @Transactional
    public void setAccess(String username, AccessControlRequest request) {
        ResourceAccess dashboardAccess = request.accessControl().get("dashboard");
        if (dashboardAccess.allowAll()) {
            aclService.clearDashboardAccess(username);
            aclService.grantAdminAccessToParent(username);
        } else {
            if (dashboardAccess.ids() == null || dashboardAccess.ids().isEmpty()) {
                aclService.clearDashboardAccess(username);
            } else {
                aclService.clearDashboardAccess(username);
                aclService.grantReadAccess(username, dashboardAccess.ids());
            }
        }
    }


    @Transactional(readOnly = true)
    public AccessControlRequest getAccess(String username) {
        boolean hasAll = aclService.hasAccessToAllDashboards(username);

        if (hasAll) {
            return new AccessControlRequest(Map.of("dashboard", new ResourceAccess(true, List.of())));
        } else {
            List<String> ids = aclService.getDashboardsWithReadAccessFor(username);
            return new AccessControlRequest(Map.of("dashboard", new ResourceAccess(false, ids)));
        }
    }
}

