package com.example.spring_acl_test.acl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.AccessControlEntry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class BitMaskPermissionGrantingStrategyTest {

    private BitMaskPermissionGrantingStrategy underTest;
    private AccessControlEntry ace;

    static class TestPermission extends BasePermission {
        protected TestPermission(int mask) {
            super(mask);
        }
    }

    @BeforeEach
    void setup() {
        AuditLogger auditLogger = Mockito.mock(AuditLogger.class);
        underTest = new BitMaskPermissionGrantingStrategy(auditLogger);
        ace = Mockito.mock(AccessControlEntry.class);
        when(ace.isGranting()).thenReturn(true); // not to confuse with isGranted that we are going to test, this is the granting_ flag in an ACl-entry

    }

    @Test
    void givenPermissions_whenRequestedEqualsAcePermission_isGranted() {
        // given
        when(ace.getPermission()).thenReturn(BasePermission.READ); // mask 1

        // when
        var requested = BasePermission.READ; // mask 1
        var granted = underTest.isGranted(ace, requested);

        // then
        assertThat(granted).isTrue();
    }

    @Test
    void givenPermissions_whenRequestedIsSubsetOfAcePermission_isGranted() {
        // given
        // WRITE (2) + READ (1) => mask 3
        when(ace.getPermission()).thenReturn(new TestPermission(3));

        // when
        var requested = BasePermission.READ; // mask 1
        var granted = underTest.isGranted(ace, requested);

        // then
        assertThat(granted).isTrue();
    }

    @Test
    void givenPermissions_whenRequestedIsNotSubsetOfAcePermission_isNotGranted() {
        // given
        when(ace.getPermission()).thenReturn(BasePermission.WRITE); // mask 2

        // when
        var requested = BasePermission.READ; // mask 1
        var granted = underTest.isGranted(ace, requested);

        // then
        assertThat(granted).isFalse();
    }

}