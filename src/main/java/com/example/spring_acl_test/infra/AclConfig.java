package com.example.spring_acl_test.infra;

import com.example.spring_acl_test.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class AclConfig {

    @Autowired
    public DataSource dataSource;

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority(Role.ADMIN.name()));
    }


    @Bean
    public SpringCacheBasedAclCache aclCache() {
        final ConcurrentMapCache aclCache = new ConcurrentMapCache("acl_cache");
        return new SpringCacheBasedAclCache(aclCache, permissionGrantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        var lookupStrategy = new BasicLookupStrategy(
                dataSource,
                aclCache(),
                aclAuthorizationStrategy(),
                permissionGrantingStrategy()
        );
        lookupStrategy.setAclClassIdSupported(true);
        return lookupStrategy;
    }

    public PermissionEvaluator permissionEvaluator(ObjectIdentityRetrievalStrategy identityRetrievalStrategy) {
        var permissionEvaluator = new AclPermissionEvaluator(aclService());
        permissionEvaluator.setObjectIdentityRetrievalStrategy(identityRetrievalStrategy);
        return new LoggingPermissionEvaluator(permissionEvaluator);
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new BitMaskPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public JdbcMutableAclService aclService() {
        var jdbcMutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
        jdbcMutableAclService.setAclClassIdSupported(true);
        return jdbcMutableAclService;
    }

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(ObjectIdentityRetrievalStrategy identityRetrievalStrategy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator(identityRetrievalStrategy));
        return expressionHandler;
    }


}
