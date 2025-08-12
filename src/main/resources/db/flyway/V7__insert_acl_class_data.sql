/*
  Insert distinct resource domain classes into acl_class if they don't exist yet.

  Explanation:
  - Select distinct reference_class_name and class_id_type from the resource table.
  - Only insert those where the reference_class_name is NOT already present in acl_class.class.
  - This prevents duplicate entries in acl_class for the same resource class.
*/
INSERT INTO acl_class (class, class_id_type)
SELECT DISTINCT
    r.reference_class_name,             -- the actual class (e.g. com.example.spring_acl_test.dashboard.Dashboard)
    r.class_id_type        -- assuming all use String IDs; adjust if needed
FROM resource r
WHERE NOT EXISTS (
    SELECT 1 FROM acl_class a WHERE a.class = r.reference_class_name
);

/*
 Example resulting acl_class table content after insertion:

 ID | class                                            | class_id_type
----+--------------------------------------------------+---------------
 1  | com.example.spring_acl_test.dashboard.Dashboard  | java.lang.String
 2  | com.example.spring_acl_test.acl.Resource         | java.lang.String
 ... (other resources inserted if present)
*/
