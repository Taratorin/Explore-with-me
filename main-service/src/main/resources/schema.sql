--DROP TABLE IF EXISTS bookings, comments, items, requests, users CASCADE;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL UNIQUE,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT uq_users UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL UNIQUE,
  CONSTRAINT pk_categories PRIMARY KEY (id),
  CONSTRAINT uq_categories UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  category_id BIGINT NOT NULL REFERENCES categories(id),
  initiator_id BIGINT NOT NULL REFERENCES users(id),
  state VARCHAR(20) NOT NULL,
  title VARCHAR(120) NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  description VARCHAR(7000) NOT NULL,
  eventDate TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  location_lat FLOAT NOT NULL,
  location_lon FLOAT NOT NULL,
  paid BOOLEAN NOT NULL,
  participantLimit INTEGER NOT NULL,
  requestModeration BOOLEAN NOT NULL,
  CONSTRAINT pk_events PRIMARY KEY (id),
  CONSTRAINT uq_events UNIQUE (title)
);

--CREATE TABLE IF NOT EXISTS requests (
--  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--  description VARCHAR(512) NOT NULL,
--  requestor_id BIGINT NOT NULL REFERENCES users(id),
--  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--  CONSTRAINT pk_requests PRIMARY KEY (id)
--);
--
--CREATE TABLE IF NOT EXISTS items (
--  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--  name VARCHAR(255) NOT NULL,
--  description VARCHAR(512) NOT NULL,
--  is_available BOOLEAN NOT NULL,
--  owner_id BIGINT NOT NULL REFERENCES users(id),
--  request_id BIGINT REFERENCES requests(id),
--  CONSTRAINT pk_items PRIMARY KEY (id)
--);
--
--CREATE TABLE IF NOT EXISTS comments (
--  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--  text VARCHAR(512) NOT NULL,
--  item_id BIGINT NOT NULL REFERENCES items(id),
--  author_id BIGINT NOT NULL REFERENCES users(id),
--  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--  CONSTRAINT pk_comments PRIMARY KEY (id)
--);
--
--CREATE TABLE IF NOT EXISTS bookings (
--  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--  end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--  item_id BIGINT NOT NULL REFERENCES items(id),
--  booker_id BIGINT NOT NULL REFERENCES users(id),
--  status VARCHAR(30) NOT NULL,
--  CONSTRAINT pk_bookings PRIMARY KEY (id)
--);