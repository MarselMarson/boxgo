CREATE TABLE IF NOT EXISTS users (
                       id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                       firstname varchar(128),
                       lastname varchar(128),
                       password varchar(128),
                       email varchar(256),
                       photo_url varchar(1024),
                       is_deleted boolean DEFAULT false NOT NULL,

                       created_at timestamptz DEFAULT current_timestamp,
                       updated_at timestamptz DEFAULT current_timestamp,
                       deleted_at timestamptz DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS verification_codes (
                     id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                     email varchar(256) NOT NULL,
                     code varchar(16) NOT NULL,
                     purpose varchar(64) NOT NULL,
                     is_used boolean not null default false,

                     created_at timestamptz DEFAULT current_timestamp,
                     expires_at timestamptz NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_email
    ON users(email);

CREATE INDEX IF NOT EXISTS idx_code
    ON verification_codes(email, code);

CREATE TABLE IF NOT EXISTS analytics_devices (
                                   id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                                   device_id bigint NOT NULL,
                                   os_type varchar(32) NOT NULL,
                                   os_version varchar(64),
                                   first_seen_at timestamptz NOT NULL DEFAULT current_timestamp, -- когда устройство впервые замечено
                                   last_seen_at timestamptz, -- последнее время активности
                                   user_id bigint REFERENCES users(id) ON DELETE SET NULL, -- если авторизован
                                   user_deleted_at timestamptz, -- если потом удалился

    -- Авторизация
                                   authCount        INTEGER NOT NULL DEFAULT 0,

    -- Поисковые запросы
                                   searchTotal      INTEGER NOT NULL DEFAULT 0,
                                   searchWithFilters INTEGER NOT NULL DEFAULT 0,

    -- Объявления
                                   adsCreated       INTEGER NOT NULL DEFAULT 0,
                                   adsEdited        INTEGER NOT NULL DEFAULT 0,
                                   adsDeleted       INTEGER NOT NULL DEFAULT 0,
                                   adsWithBookings  INTEGER NOT NULL DEFAULT 0,

    -- Сообщения
                                   messagesSent     INTEGER NOT NULL DEFAULT 0,
                                   messagesReceived INTEGER NOT NULL DEFAULT 0
);
