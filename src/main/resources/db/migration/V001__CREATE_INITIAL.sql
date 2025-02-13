CREATE SEQUENCE  IF NOT EXISTS primary_sequence START WITH 10000 INCREMENT BY 1;

CREATE TABLE ball_info (
    ball_id BIGINT NOT NULL,
    ball_name VARCHAR(255) NOT NULL,
    ball_number INTEGER NOT NULL,
    ball_time TIMESTAMP WITHOUT TIME ZONE,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT ball_info_pkey PRIMARY KEY (ball_id)
);

CREATE TABLE ball_detail (
    id BIGINT NOT NULL,
    color VARCHAR(255) NOT NULL,
    ball_info_id BIGINT,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT ball_detail_pkey PRIMARY KEY (id)
);

CREATE TABLE user_info (
    id BIGINT NOT NULL,
    email VARCHAR(320) NOT NULL,
    is_active BOOLEAN NOT NULL,
    credentials VARCHAR(150),
    user_role VARCHAR(505),
    reset_string VARCHAR(255),
    reset_time TIMESTAMP WITHOUT TIME ZONE,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT user_info_pkey PRIMARY KEY (id)
);

ALTER TABLE ball_detail ADD CONSTRAINT fk_ball_detail_ball_info_id FOREIGN KEY (ball_info_id) REFERENCES ball_info (ball_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE user_info ADD CONSTRAINT unique_user_info_email UNIQUE (email);
