package com.example.spring_acl_test.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.model.AclService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AccessControlService accessControlService;

    @GetMapping("/{username}")
    public ResponseEntity<AccessControlRequest> getPermissions(@PathVariable String username) {
        return ResponseEntity.ok(accessControlService.getAccess(username));
    }

    @PostMapping("/{username}")
    public ResponseEntity<Void> setPermissions(@PathVariable String username,
                                               @RequestBody AccessControlRequest request) {
        accessControlService.setAccess(username, request);
        return ResponseEntity.noContent().build();
    }
}
