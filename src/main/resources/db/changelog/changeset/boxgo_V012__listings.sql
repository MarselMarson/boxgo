CREATE TABLE IF NOT EXISTS listings (
          id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
          owner_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,

          first_from_city_id     BIGINT NOT NULL REFERENCES city(id),
          first_departure_local  TIMESTAMP NOT NULL,
          last_to_city_id        BIGINT NOT NULL REFERENCES city(id),
          last_arrival_local     TIMESTAMP NOT NULL,

          segments_count smallint NOT NULL,

          is_archived BOOLEAN NOT NULL DEFAULT FALSE,

          created_at TIMESTAMPTZ DEFAULT current_timestamp,
          updated_at TIMESTAMPTZ DEFAULT current_timestamp,
          archived_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_listings_next_departure ON listings (first_departure_local)
    WHERE is_archived = FALSE;

CREATE INDEX IF NOT EXISTS idx_listings_active ON listings (owner_id)
    WHERE is_archived = FALSE;



CREATE TABLE IF NOT EXISTS route_segments (
        id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
        listing_id bigint NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
        owner_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,

        segment_order SMALLINT,
        total_segments_count smallint NOT NULL,

        departure_city_id INTEGER NOT NULL REFERENCES city(id),
        departure_at TIMESTAMPTZ NOT NULL,
        departure_local_at TIMESTAMP NOT NULL,

        arrival_city_id INTEGER NOT NULL REFERENCES city(id),
        arrival_at TIMESTAMPTZ,
        arrival_local_at TIMESTAMPTZ,

        is_archived BOOLEAN NOT NULL DEFAULT FALSE,

        archive_at TIMESTAMPTZ,
        created_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
        updated_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
        archived_at TIMESTAMPTZ,

        available_item_count       SMALLINT NOT NULL DEFAULT 0,
        available_envelope_count   SMALLINT NOT NULL DEFAULT 0,
        available_box_count        SMALLINT NOT NULL DEFAULT 0,
        available_carry_on_count   SMALLINT NOT NULL DEFAULT 0,
        available_baggage_count    SMALLINT NOT NULL DEFAULT 0,
        available_oversized_count  SMALLINT NOT NULL DEFAULT 0,

        available_total_count SMALLINT GENERATED ALWAYS AS (
                        available_item_count +
                        available_envelope_count +
                        available_box_count +
                        available_carry_on_count +
                        available_baggage_count +
                        available_oversized_count
                        ) STORED,

        CONSTRAINT counts_nonneg_ck CHECK (
            available_item_count      >= 0 AND
            available_envelope_count  >= 0 AND
            available_box_count       >= 0 AND
            available_carry_on_count  >= 0 AND
            available_baggage_count   >= 0 AND
            available_oversized_count >= 0
            )
);

CREATE INDEX IF NOT EXISTS idx_route_segments_departure_at
    ON route_segments (departure_local_at, arrival_local_at, id)
    WHERE is_archived = FALSE AND available_total_count > 0;

CREATE INDEX IF NOT EXISTS idx_route_segments_departure_city_and_date
    ON route_segments (departure_city_id, departure_local_at, arrival_local_at, id)
    WHERE is_archived = FALSE AND available_total_count > 0;

CREATE INDEX IF NOT EXISTS idx_route_segments_arrival_city_and_date
    ON route_segments (arrival_city_id, departure_local_at, arrival_local_at, id)
    WHERE is_archived = FALSE AND available_total_count > 0;

CREATE INDEX IF NOT EXISTS idx_route_segments_departure_and_arrival_city_and_date
    ON route_segments (departure_city_id, arrival_city_id, departure_local_at, arrival_local_at, id)
    WHERE is_archived = FALSE AND available_total_count > 0;

CREATE INDEX IF NOT EXISTS idx_route_segments_listing
    ON route_segments (listing_id, segment_order);

CREATE INDEX IF NOT EXISTS idx_route_segments_archive_at
    ON route_segments (archive_at);