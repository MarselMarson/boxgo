CREATE TABLE IF NOT EXISTS categories (
          id bigint PRIMARY KEY,
          name varchar(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS parcel_types (
            id bigint PRIMARY KEY,
            name varchar(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS packages (
          id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,

          listing_id bigint NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
          parcel_type_id  bigint NOT NULL REFERENCES parcel_types(id) ON DELETE CASCADE,

          length int,
          width int,
          height int,
          weight int,
          quantity int,
          price int,

          is_archived BOOLEAN NOT NULL DEFAULT FALSE,

          created_at TIMESTAMPTZ DEFAULT current_timestamp,
          updated_at TIMESTAMPTZ DEFAULT current_timestamp,
          deleted_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_listing_packages_active ON packages (listing_id)
    WHERE is_archived = FALSE;

CREATE TABLE IF NOT EXISTS package_categories (
          package_id bigint,
          category_id bigint,
          created_at timestamptz DEFAULT current_timestamp,

          PRIMARY KEY (package_id, category_id),
          CONSTRAINT fk_package_key FOREIGN KEY (package_id) REFERENCES packages (id) ON DELETE CASCADE,
          CONSTRAINT fk_category_key FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);