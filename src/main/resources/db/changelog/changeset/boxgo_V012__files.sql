CREATE TABLE IF NOT EXISTS files (
             id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
             name varchar(64),
             url varchar(256),

             created_at timestamptz DEFAULT current_timestamp,
             updated_at timestamptz DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS user_files (
              user_id bigint,
              file_id bigint,
              created_at timestamptz DEFAULT current_timestamp,

              PRIMARY KEY (user_id, file_id),
              CONSTRAINT fk_user_key FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
              CONSTRAINT fk_file_key FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);