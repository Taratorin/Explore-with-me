DROP TABLE IF EXISTS
events_compilations, compilations, participation_requests, complaints_comments,
 complaints, likes, comments, events, categories, users CASCADE;

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
  event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  published_on TIMESTAMP WITHOUT TIME ZONE,
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INTEGER NOT NULL,
  request_moderation BOOLEAN NOT NULL,
  CONSTRAINT pk_events PRIMARY KEY (id),
  CONSTRAINT uq_events UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  event_id BIGINT NOT NULL REFERENCES events(id),
  author_id BIGINT NOT NULL REFERENCES users(id),
  title VARCHAR(100) NOT NULL,
  text VARCHAR(1000) NOT NULL,
  ts TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  is_edited BOOLEAN NOT NULL,
  ts_edition TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_comments PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS likes (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  comment_id BIGINT NOT NULL REFERENCES comments(id),
  user_id BIGINT NOT NULL REFERENCES users(id),
  like_dislike INTEGER NOT NULL,
  CONSTRAINT pk_likes PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS complaints (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  comment_id BIGINT NOT NULL REFERENCES comments(id),
  complainer_id BIGINT NOT NULL REFERENCES users(id),
  text VARCHAR(200) NOT NULL,
  state VARCHAR(10) NOT NULL,
  CONSTRAINT pk_complaints PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS complaints_comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  comment_id BIGINT REFERENCES comments(id),
  complaint_id BIGINT REFERENCES complaints(id),
  CONSTRAINT pk_complaints_comments PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events_comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  comment_id BIGINT REFERENCES comments(id),
  event_id BIGINT REFERENCES events(id),
  CONSTRAINT pk_events_comments PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS participation_requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  event_id BIGINT NOT NULL REFERENCES events(id),
  requester_id BIGINT NOT NULL REFERENCES users(id),
  status VARCHAR(20) NOT NULL,
  CONSTRAINT pk_participation_requests PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  events BIGINT REFERENCES events(id),
  pinned BOOLEAN NOT NULL,
  title VARCHAR(50) NOT NULL,
  CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events_compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  compilation BIGINT REFERENCES compilations(id),
  events_id BIGINT REFERENCES events(id),
  CONSTRAINT pk_events_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  event_id BIGINT NOT NULL REFERENCES events(id),
  author_id BIGINT NOT NULL REFERENCES users(id),
  title VARCHAR(100) NOT NULL,
  text VARCHAR(1000) NOT NULL,
  ts TIMESTAMP WITHOUT TIME ZONE,
  likes BIGINT,
  dislikes BIGINT,
  is_edited BOOLEAN NOT NULL,
  ts_edition TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_comments PRIMARY KEY (id)
);