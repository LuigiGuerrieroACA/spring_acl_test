package com.example.spring_acl_test.acl;


import com.example.spring_acl_test.dashboard.Dashboard;
import com.example.spring_acl_test.dashboard.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ACLService {

    private final JdbcMutableAclService jdbcMutableAclService;
    private final DashboardRepository dashboardService;
    private static final Permission ALL_RIGHTS = new Permission() {
        @Override
        public int getMask() {
            return 31;
        }

        @Override
        public String getPattern() {
            return "";
        }
    };

    @Transactional
    public void add(Dashboard saved) {
        ObjectIdentity oid = new ObjectIdentityImpl(Dashboard.class, saved.getId());
        MutableAcl acl = jdbcMutableAclService.createAcl(oid);

        ObjectIdentity parentOid = new ObjectIdentityImpl(Resource.class, "DASHBOARD");
        acl.setOwner(new PrincipalSid("admin"));
        acl.setParent(jdbcMutableAclService.readAclById(parentOid));
        acl.setEntriesInheriting(true);
        jdbcMutableAclService.updateAcl(acl);
    }

    @Transactional
    public void delete(String id) {
        ObjectIdentity oid = new ObjectIdentityImpl(Dashboard.class, id);
        jdbcMutableAclService.deleteAcl(oid, false);
    }


    @Transactional(readOnly = true)
    public boolean hasAccessToAllDashboards(String username) {
        ObjectIdentity parentOid = new ObjectIdentityImpl(Resource.class, "DASHBOARD");
        try {
            Acl acl = jdbcMutableAclService.readAclById(parentOid, List.of(new PrincipalSid(username)));
            return acl.isGranted(
                    List.of(BasePermission.ADMINISTRATION, BasePermission.READ, ALL_RIGHTS),
                    List.of(new PrincipalSid(username)),
                    false
            );
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<String> getDashboardsWithReadAccessFor(String username) {
        PrincipalSid sid = new PrincipalSid(username);
        return findAllIds().stream()
                .filter(id -> hasReadPermission(id, sid))
                .toList();
    }

    private boolean hasReadPermission(String id, PrincipalSid sid) {
        ObjectIdentity oid = new ObjectIdentityImpl(Dashboard.class, id);
        try {
            Acl acl = jdbcMutableAclService.readAclById(oid, List.of(sid));
            return acl.isGranted(List.of(BasePermission.READ), List.of(sid), false);
        } catch (NotFoundException e) {
            return false;
        }
    }


    @Transactional
    public void grantReadAccess(String username, List<String> dashboardIds) {
        Sid sid = new PrincipalSid(username);

        dashboardIds.stream()
                .map(id -> new ObjectIdentityImpl(Dashboard.class, id))
                .map(this::getOrCreateAcl)
                .forEach(acl -> {
                    clearPermissionsForSid(acl, sid);
                    acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid, true);
                    jdbcMutableAclService.updateAcl(acl);
                });
    }


    @Transactional
    public void grantAdminAccessToParent(String username) {
        Sid sid = new PrincipalSid(username);
        ObjectIdentity parentOid = new ObjectIdentityImpl(Resource.class, "DASHBOARD");
        MutableAcl acl = (MutableAcl) jdbcMutableAclService.readAclById(parentOid);
        clearPermissionsForSid(acl, sid);
        acl.insertAce(acl.getEntries().size(), ALL_RIGHTS, sid, true);
        jdbcMutableAclService.updateAcl(acl);
    }

    private MutableAcl getOrCreateAcl(ObjectIdentity oid) {
        try {
            return (MutableAcl) jdbcMutableAclService.readAclById(oid);
        } catch (NotFoundException e) {
            return jdbcMutableAclService.createAcl(oid);
        }
    }

    @Transactional
    public void clearDashboardAccess(String username) {
        Sid sid = new PrincipalSid(username);

        // Remove parent ACE
        ObjectIdentity parentOid = new ObjectIdentityImpl(Resource.class, "DASHBOARD");
        try {
            MutableAcl parentAcl = (MutableAcl) jdbcMutableAclService.readAclById(parentOid);
            clearPermissionsForSid(parentAcl, sid);
            jdbcMutableAclService.updateAcl(parentAcl);
        } catch (NotFoundException ignore) {
        }

        // Remove per-dashboard ACEs
        findAllIds().stream()
                .map(id -> new ObjectIdentityImpl(Dashboard.class, id))
                .forEach(oid -> {
                    try {
                        MutableAcl acl = (MutableAcl) jdbcMutableAclService.readAclById(oid);
                        clearPermissionsForSid(acl, sid);
                        jdbcMutableAclService.updateAcl(acl);
                    } catch (NotFoundException ignore) {
                    }
                });
    }

    private void clearPermissionsForSid(MutableAcl acl, Sid sid) {
        IntStream.range(0, acl.getEntries().size())
                .map(i -> acl.getEntries().size() - 1 - i)
                .filter(i -> acl.getEntries().get(i).getSid().equals(sid))
                .forEach(acl::deleteAce);
    }


    public List<String> findAllIds() {
        return dashboardService.findAll().stream().map(Dashboard::getId).toList();
    }

}
