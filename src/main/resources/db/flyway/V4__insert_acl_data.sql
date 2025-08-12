INSERT INTO acl_sid (principal, sid)
SELECT TRUE, name FROM users u
WHERE NOT EXISTS (
    SELECT 1 FROM acl_sid a WHERE a.sid = u.name
);

INSERT INTO acl_class (class, class_id_type)
SELECT 'com.example.spring_acl_test.dashboard.Dashboard', 'java.lang.String'
FROM (SELECT 1) AS dummy
WHERE NOT EXISTS (
    SELECT 1 FROM acl_class WHERE class = 'com.example.spring_acl_test.dashboard.Dashboard'
);

INSERT INTO acl_object_identity (object_id_class, object_id_identity, owner_sid, entries_inheriting)
VALUES (
           (SELECT id FROM acl_class WHERE class = 'com.example.spring_acl_test.dashboard.Dashboard'),
           'DASHBOARD',
           (SELECT id FROM acl_sid WHERE sid = 'admin' AND principal = TRUE),
           TRUE
       );

INSERT INTO acl_object_identity (object_id_class, object_id_identity, owner_sid, entries_inheriting, parent_object)
SELECT
    (SELECT id FROM acl_class WHERE class = 'com.example.spring_acl_test.dashboard.Dashboard'),
    CAST(d.id AS VARCHAR(255)),
    (SELECT id FROM acl_sid WHERE sid = 'admin' AND principal = TRUE),
    TRUE,
    -- Here we get the id of the parent ACL object identity for DASHBOARD
    (SELECT id FROM acl_object_identity WHERE object_id_class = (SELECT id FROM acl_class WHERE class = 'com.example.spring_acl_test.dashboard.Dashboard')
                                          AND object_id_identity = 'DASHBOARD')
FROM dashboards d
WHERE NOT EXISTS (
    SELECT 1 FROM acl_object_identity oi WHERE oi.object_id_identity = CAST(d.id AS VARCHAR(255))
);



-- INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
-- SELECT
--     oi.id,
--     0,  -- ace_order (order of this ACE)
--     (SELECT id FROM acl_sid WHERE sid = 'admin' AND principal = TRUE),
--     31, -- full permissions mask
--     TRUE,
--     FALSE,
--     FALSE
-- FROM acl_object_identity oi
-- WHERE NOT EXISTS (
--     SELECT 1 FROM acl_entry e
--     WHERE e.acl_object_identity = oi.id
--       AND e.sid = (SELECT id FROM acl_sid WHERE sid = 'admin' AND principal = TRUE)
--;

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES (1, 0, 1, 31, true, false, false);

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT
    oi.id,
    0,
    (SELECT id FROM acl_sid WHERE sid = 'user1' AND principal = TRUE),
    1, -- READ permission
    TRUE,
    FALSE,
    FALSE
FROM acl_object_identity oi
WHERE oi.object_id_identity = '550e8400-e29b-41d4-a716-446655450000'
  AND NOT EXISTS (
    SELECT 1 FROM acl_entry e
    WHERE e.acl_object_identity = oi.id
      AND e.sid = (SELECT id FROM acl_sid WHERE sid = 'user1' AND principal = TRUE)
);

CREATE OR REPLACE VIEW acl_class_view AS
SELECT
    id,
    class AS class_id_type
FROM acl_class;