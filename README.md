# Demo ACL Project

This is a simple Spring Boot demo project demonstrating ACL (Access Control List) permissions with custom bitmask permission granting strategy.

## Sample Data

The database contains the following initial users and dashboards:

### Users

| ID                                   | Name  | Password | Role  |
|------------------------------------|--------|----------|-------|
| 550e8400-e29b-41d4-a716-446655440000 | admin  | admin    | ADMIN |
| 550e8400-e29b-41d4-a716-446655440001 | user1  | user1    | USER  |

### Dashboards

| ID                                   | Name        |
|------------------------------------|-------------|
| 550e8400-e29b-41d4-a716-446655450000 | Dashboard 1 |
| 550e8400-e29b-41d4-a716-446655440001 | Dashboard 2 |

## Usage

The project exposes REST endpoints to retrieve dashboards, secured by Spring Security ACL with permission checks.

### Endpoints

- `GET /dashboards`  
  Returns all dashboards accessible by the authenticated user.  
  Uses `@PostFilter` to only return dashboards the user has **READ** permission for.

- `GET /dashboards/{id}`  
  Returns a single dashboard by ID if the user has **READ** permission for that dashboard.

### Authentication

Use HTTP Basic Authentication with one of the predefined users:

- `admin` / `admin` (has full permissions)
- `user1` / `user1` (has limited permissions)

### Examples

Fetch Dashboard 1 as user1 (should succeed if user1 has READ permission):

```bash
curl -u user1:user1 http://localhost:8080/dashboards/550e8400-e29b-41d4-a716-446655450000
