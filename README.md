# Demo Spring Boot ACL Project

This project demonstrates using Spring Security ACL with custom bitwise permission evaluation.

---

## Initial Data Setup

The database contains the following users and dashboards:

### Users

| id                                   | name  | password | role  |
|------------------------------------|-------|----------|-------|
| 550e8400-e29b-41d4-a716-446655440000 | admin | admin    | ADMIN |
| 550e8400-e29b-41d4-a716-446655440001 | user1 | user1    | USER  |

### Dashboards

| id                                   | name        |
|------------------------------------|-------------|
| 550e8400-e29b-41d4-a716-446655450000 | Dashboard 1 |
| 550e8400-e29b-41d4-a716-446655440001 | Dashboard 2 |

---

## Usage

The REST endpoints for dashboards:

```bash
# Get all dashboards you have READ access to (filtered by ACL)
curl -u user1:user1 http://localhost:8080/dashboards

# Get dashboard by ID with ACL check on READ permission (user1 only has permission for Dashboard 1)
curl -u user1:user1 http://localhost:8080/dashboards/550e8400-e29b-41d4-a716-446655450000

# Or use admin credentials for full access
curl -u admin:admin http://localhost:8080/dashboards
curl -u admin:admin http://localhost:8080/dashboards/550e8400-e29b-41d4-a716-446655440001
```


---

## ACL Permission Notes

We use a **bitwise permission mask** to represent multiple permissions in a single Access Control Entry (ACE).

| Permission      | Bit Value | Binary Mask |
|-----------------|-----------|-------------|
| READ            | 1         | 00001       |
| WRITE           | 2         | 00010       |
| CREATE          | 4         | 00100       |
| DELETE          | 8         | 01000       |
| ADMINISTRATION  | 16        | 10000       |
| **Full access** | **31**    | 11111       |

### ACL Entries in the `acl_entry` Table

| id | acl_object_identity | ace_order | sid | mask | granting | audit_success | audit_failure |
|----|---------------------|-----------|-----|------|----------|---------------|--------------|
| 1  | 1                   | 0         | 1   | 31   | true     | false         | false        |
| 2  | 2                   | 0         | 1   | 31   | true     | false         | false        |
| 3  | 1                   | 0         | 2   | 1    | true     | false         | false        |

- `acl_object_identity` 1 and 2 correspond to **Dashboard 1** and **Dashboard 2**, respectively.
- `sid` 1 corresponds to the **admin** user, with full permission mask `31` (all permissions).
- `sid` 2 corresponds to **user1**, with only the READ permission mask `1` on Dashboard 1.
- This means the admin has full access to both dashboards with only one ACE per dashboard.
- **Bitwise evaluation** allows representing multiple permissions in one mask instead of multiple separate ACEs.

- ---

## Bitwise Permission Evaluation

Spring Security ACL by default compares permission masks with strict equality, which limits combining multiple permissions in one ACE.

To fix this, this project uses a **custom `PermissionGrantingStrategy`** that performs bitwise checks:

```java
private boolean containsPermission(int mask, int permission) {
    return (mask & permission) == permission;
}
```

