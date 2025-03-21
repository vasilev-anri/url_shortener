CREATE TABLE url
(
    id           INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    original_url VARCHAR(512)       NOT NULL,
    short_url    varchar(32) UNIQUE NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    access_count BIGINT    DEFAULT 0,
    user_id      INT                NOT NULL REFERENCES app_user (user_id) ON DELETE CASCADE
);


CREATE TABLE app_user
(
    user_id  INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(56)  NOT NULL,
    password VARCHAR(120) NOT NULL,
    enabled  BOOLEAN      NOT NULL,
    role     VARCHAR(50)  NOT NULL DEFAULT 'ROLE_USER'
);

CREATE TABLE role
(
    role_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id INT         NOT NULL REFERENCES app_user (user_id) ON DELETE CASCADE,
    role    VARCHAR(50) NOT NULL
);



CREATE OR REPLACE FUNCTION sync_user_role() RETURNS TRIGGER AS
$$
BEGIN
    IF NOT pg_trigger_depth() > 1 THEN
        DELETE FROM role WHERE user_id = NEW.user_id;
        INSERT INTO role (user_id, role) VALUES (NEW.user_id, NEW.role);
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER sync_user_role_trigger
    AFTER INSERT OR UPDATE
    ON app_user
    FOR EACH ROW
EXECUTE FUNCTION sync_user_role();



CREATE OR REPLACE FUNCTION sync_app_user_role() RETURNS TRIGGER AS
$$
BEGIN
    IF NOT pg_trigger_depth() > 1 THEN
        UPDATE app_user
        SET role = NEW.role
        WHERE user_id = NEW.user_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER sync_app_user_role_trigger
    AFTER INSERT OR UPDATE
    ON role
    FOR EACH ROW
EXECUTE FUNCTION sync_app_user_role();

CREATE OR REPLACE FUNCTION delete_old_urls() RETURNS VOID AS
$$
BEGIN

    DELETE FROM url WHERE created_at < NOW() - INTERVAL '30 days';

END;
$$ LANGUAGE plpgsql;
