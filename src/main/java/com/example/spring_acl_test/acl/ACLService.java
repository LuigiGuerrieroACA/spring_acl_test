package com.example.spring_acl_test.acl;


import com.example.spring_acl_test.dashboard.Dashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ACLService {

    private final JdbcMutableAclService jdbcMutableAclService;

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

}
