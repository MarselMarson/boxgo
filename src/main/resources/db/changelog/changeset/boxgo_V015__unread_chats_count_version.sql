CREATE TABLE IF NOT EXISTS unread_chats_count_version (
        user_id bigint PRIMARY KEY,
        version BIGINT NOT NULL DEFAULT 0,

        CONSTRAINT fk_user_id FOREIGN KEY (user_id)
            REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS unread_chats_user_version
    ON unread_chats_count_version (user_id, version);