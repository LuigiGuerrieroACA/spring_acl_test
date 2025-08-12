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