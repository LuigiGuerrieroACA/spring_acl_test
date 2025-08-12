INSERT INTO acl_sid (principal, sid)
SELECT TRUE, name FROM users u
WHERE NOT EXISTS (
    SELECT 1 FROM acl_sid a WHERE a.sid = u.name
);
