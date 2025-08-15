CREATE TABLE listings (
          id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
          owner_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
          is_archived BOOLEAN NOT NULL DEFAULT FALSE,
          has_available_packages BOOLEAN NOT NULL DEFAULT TRUE,
          auto_confirm_bookings BOOLEAN NOT NULL DEFAULT FALSE,
          next_departure_at TIMESTAMPTZ,
          created_at TIMESTAMPTZ DEFAULT current_timestamp,
          updated_at TIMESTAMPTZ DEFAULT current_timestamp
);

CREATE INDEX idx_listings_next_departure ON listings (next_departure_at)
    WHERE is_archived = FALSE AND has_available_packages = TRUE;

CREATE INDEX idx_listings_active ON listings (owner_id)
    WHERE is_archived = FALSE;

CREATE TABLE route_segments (
        id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
        listing_id bigint NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
        departure_city_id INTEGER NOT NULL REFERENCES city(id),
        departure_at TIMESTAMPTZ NOT NULL,
        arrival_city_id INTEGER NOT NULL REFERENCES city(id),
        arrival_at TIMESTAMPTZ,
        segment_order SMALLINT,
        is_archived BOOLEAN NOT NULL DEFAULT FALSE,
        created_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
        updated_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp
);

CREATE INDEX idx_route_segments_cities_dates
    ON route_segments (departure_city_id, arrival_city_id, departure_at)
    WHERE is_archived = FALSE;

CREATE INDEX idx_route_segments_listing ON route_segments (listing_id, segment_order)
    WHERE is_archived = FALSE;

CREATE INDEX idx_route_segments_departure_at ON route_segments (arrival_city_id, departure_at)
    WHERE is_archived = FALSE;

CREATE INDEX idx_route_segments_departure_at ON route_segments (departure_at)
    WHERE is_archived = FALSE;