package com.example.spring_acl_test.dashboard;

import com.example.spring_acl_test.acl.ACLService;
import com.example.spring_acl_test.user.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {

    private final DashboardRepository repository;
    private final ACLService aclService;

    public DashboardService(DashboardRepository repository, ACLService aclService) {
        this.repository = repository;
        this.aclService = aclService;
    }

    public List<Dashboard> findAll() {
        return repository.findAll();
    }

    public Optional<Dashboard> findById(String id) {
        return repository.findById(id);
    }

    public Dashboard save(Dashboard dashboard) {
        return repository.save(dashboard);
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
        aclService.delete(id);
    }


    @Transactional
    public Dashboard saveNew() {
        var numberOfDashboards = repository.count();
        var newDashboard = new Dashboard(null, "Dashboard %s".formatted(numberOfDashboards + 1));
        var saved = save(newDashboard);

        Authentication original = SecurityContextHolder.getContext().getAuthentication();
        try {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            "admin",
                            "admin",
                            List.of(new SimpleGrantedAuthority(Role.ADMIN.name()))
                    )
            );
            aclService.add(saved);
            return saved;
        } finally {
            SecurityContextHolder.getContext().setAuthentication(original);
        }
    }





}
