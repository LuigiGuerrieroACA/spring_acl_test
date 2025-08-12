CREATE TABLE users (
                       id VARCHAR PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

CREATE TABLE dashboards (
                            id VARCHAR PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

CREATE TABLE resource (
                          id VARCHAR(50) PRIMARY KEY,
                          reference_class_name VARCHAR(255) NOT NULL,
                          class_id_type VARCHAR(255) NOT NULL
);

