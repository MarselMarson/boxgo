CREATE TABLE IF NOT EXISTS chats (
        id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
        user_id bigint NOT NULL,
        listing_owner_id bigint NOT NULL,
        listing_id bigint NOT NULL,
        segment_id BIGINT NOT NULL,

        from_city_id BIGINT NOT NULL,
        to_city_id BIGINT NOT NULL,
        departure_local_at TIMESTAMP NOT NULL,
        arrival_local_at TIMESTAMP NOT NULL,

        unread_messages_count BIGINT NOT NULL DEFAULT 0,

        is_active boolean DEFAULT true NOT NULL,

        version BIGINT NOT NULL DEFAULT 0,

        created_at timestamptz DEFAULT current_timestamp,
        updated_at timestamptz DEFAULT current_timestamp,

        CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id),
        CONSTRAINT fk_listing_owner_id FOREIGN KEY (listing_owner_id) REFERENCES users (id),
        CONSTRAINT fk_listing_id FOREIGN KEY (listing_id) REFERENCES listings (id) ON DELETE CASCADE,
        CONSTRAINT fk_segment_id FOREIGN KEY (segment_id) REFERENCES route_segments (id) ON DELETE CASCADE,

        CONSTRAINT fk_from_city_id FOREIGN KEY (from_city_id) REFERENCES city (id) ON DELETE CASCADE,
        CONSTRAINT fk_to_city_id FOREIGN KEY (to_city_id) REFERENCES city (id) ON DELETE CASCADE,

        CONSTRAINT unique_user_segment UNIQUE (user_id, segment_id)
);

CREATE TABLE IF NOT EXISTS chat_messages (
        id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
        chat_id bigint NOT NULL,
        sender_id bigint NOT NULL,
        recipient_id bigint NOT NULL,
        content varchar(4096) NOT NULL,
        status varchar(32) NOT NULL,

        frontend_id UUID,
        version BIGINT NOT NULL DEFAULT 0,

        created_at timestamptz DEFAULT current_timestamp,
        updated_at timestamptz DEFAULT current_timestamp,

        CONSTRAINT fk_chat_id FOREIGN KEY (chat_id) REFERENCES chats (id) ON DELETE CASCADE,
        CONSTRAINT fk_sender_id FOREIGN KEY (sender_id) REFERENCES users (id),
        CONSTRAINT fk_recipient_id FOREIGN KEY (recipient_id) REFERENCES users (id)
);

ALTER TABLE chats
    ADD COLUMN last_message_id BIGINT,
    ADD COLUMN last_message_sender_id BIGINT,
    ADD COLUMN last_message_content varchar(4096),
    ADD COLUMN last_message_created_at TIMESTAMPTZ,
    ADD CONSTRAINT fk_last_message_id FOREIGN KEY (last_message_id) REFERENCES chat_messages (id),
    ADD CONSTRAINT fk_last_message_sender_id FOREIGN KEY (last_message_sender_id) REFERENCES users (id);

CREATE INDEX IF NOT EXISTS idx_chats_user_id_last_message_date
    ON chats(user_id, last_message_created_at DESC);
CREATE INDEX IF NOT EXISTS idx_chats_user_id_segment_id
    ON chats(user_id, segment_id);
CREATE INDEX IF NOT EXISTS idx_chats_listing_owner_id_last_message_date
    ON chats(listing_owner_id, last_message_created_at DESC);
CREATE INDEX IF NOT EXISTS idx_chats_listing_owner_id_user_id
    ON chats(listing_owner_id, user_id);
CREATE INDEX IF NOT EXISTS idx_chat_messages_chat_id_id
    ON chat_messages(chat_id, id DESC);
CREATE INDEX IF NOT EXISTS idx_chat_messages_status_recipient_chat
    ON chat_messages(chat_id, recipient_id, status);
