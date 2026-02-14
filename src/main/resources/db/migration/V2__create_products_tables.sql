CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE products (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    price       NUMERIC(12,2) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL
);

CREATE TABLE product_sizes (
    id             UUID PRIMARY KEY,
    product_id     UUID NOT NULL REFERENCES products(id),
    size_label     VARCHAR(20) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ NOT NULL
);

CREATE TABLE product_images (
    id         UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products(id),
    image_url  TEXT NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);