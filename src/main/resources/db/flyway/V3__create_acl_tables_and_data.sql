CREATE TABLE acl_sid (
                         id BIGSERIAL PRIMARY KEY,
                         principal BOOLEAN NOT NULL,
                         sid VARCHAR(100) NOT NULL,
                         CONSTRAINT unique_acl_sid UNIQUE (sid, principal)
);

CREATE TABLE acl_class (
                           id BIGSERIAL PRIMARY KEY,
                           class VARCHAR(255) NOT NULL,
                           class_id_type VARCHAR(255) NOT NULL,
                           CONSTRAINT unique_acl_class UNIQUE (class)
);

CREATE TABLE acl_object_identity (
                                     id BIGSERIAL PRIMARY KEY,
                                     object_id_class BIGINT NOT NULL,
                                     object_id_identity VARCHAR(255) NOT NULL,
                                     parent_object BIGINT,
                                     owner_sid BIGINT,
                                     entries_inheriting BOOLEAN NOT NULL,
                                     CONSTRAINT unique_object_identity UNIQUE (object_id_class, object_id_identity),
                                     CONSTRAINT fk_object_class FOREIGN KEY (object_id_class) REFERENCES acl_class (id),
                                     CONSTRAINT fk_parent_object FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id),
                                     CONSTRAINT fk_owner FOREIGN KEY (owner_sid) REFERENCES acl_sid (id)
);

-- 4. acl_entry
CREATE TABLE acl_entry (
                           id BIGSERIAL PRIMARY KEY,
                           acl_object_identity BIGINT NOT NULL,
                           ace_order INT NOT NULL,
                           sid BIGINT NOT NULL,
                           mask INTEGER NOT NULL,
                           granting BOOLEAN NOT NULL,
                           audit_success BOOLEAN NOT NULL,
                           audit_failure BOOLEAN NOT NULL,
                           CONSTRAINT unique_acl_entry UNIQUE (acl_object_identity, ace_order, sid),
                           CONSTRAINT fk_acl_entry_object FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity (id),
                           CONSTRAINT fk_acl_entry_sid FOREIGN KEY (sid) REFERENCES acl_sid (id)
);
