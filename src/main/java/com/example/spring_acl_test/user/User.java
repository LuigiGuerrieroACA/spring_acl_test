package com.example.spring_acl_test.user;

import com.example.spring_acl_test.infra.ACLEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
public class User implements ACLEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String passwordHashed;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public User()
    {
    }

    public User(String name, String passwordHashed, Role role)
    {
        this.name = name;
        this.passwordHashed = passwordHashed;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHashed() {
        return passwordHashed;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getACLId() {
        return "ACL-" + id;
    }
}