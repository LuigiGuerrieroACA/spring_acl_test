--  Insert ACL_OBJECT_IDENTITY rows for each dashboard as child entries.
-- ID | OBJECT_ID_CLASS | OBJECT_IDENTITY                         | PARENT_OBJECT | OWNER_SID | ENTRIES_INHERITING
-- ----------------------------------------------------------------------------------------------------------
-- 1  | 2               | DASHBOARD                               | NULL          | 1         | TRUE
-- 3  | 1               | 550e8400-e29b-41d4-a716-446655450000    | 1             | 1         | TRUE
-- 4  | 1               | 550e8400-e29b-41d4-a716-446655450001    | 1             | 1         | TRUE
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT
    dashboard_acl_class.id,
    d.id,
    parent_oi.id,
    1,
    TRUE
FROM dashboards d
         JOIN acl_class dashboard_acl_class ON dashboard_acl_class.class = 'com.example.spring_acl_test.dashboard.Dashboard'
         JOIN acl_object_identity parent_oi ON
    parent_oi.object_id_identity = 'DASHBOARD'
        AND parent_oi.object_id_class = (
        SELECT id FROM acl_class WHERE class = 'com.example.spring_acl_test.acl.Resource'
    )
WHERE NOT EXISTS (
    SELECT 1 FROM acl_object_identity aoi
    WHERE aoi.object_id_identity = d.id
      AND aoi.object_id_class = dashboard_acl_class.id
);