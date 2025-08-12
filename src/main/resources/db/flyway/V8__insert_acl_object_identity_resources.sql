--  Insert ACL_OBJECT_IDENTITY rows for all resources as parent entries.
-- result after this step:
-- ID | OBJECT_ID_CLASS | OBJECT_IDENTITY | PARENT_OBJECT | OWNER_SID | ENTRIES_INHERITING
-- ------------------------------------------------------------------------------
-- 1  | 2               | DASHBOARD       | NULL          | 1         | TRUE
-- 2  | 2               | SOURCE          | NULL          | 1         | TRUE
-- ... (one row per resource)
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT
    acl_class.id,
    r.id,
    NULL,
    1,
    TRUE
FROM resource r
         JOIN acl_class acl_class ON acl_class.class = r.reference_class_name
WHERE NOT EXISTS (
    SELECT 1 FROM acl_object_identity aoi
    WHERE aoi.object_id_identity = r.id
      AND aoi.object_id_class = acl_class.id
);





