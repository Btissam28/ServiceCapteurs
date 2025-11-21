CREATE TABLE container
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    container_id VARCHAR(255) NOT NULL,
    capacity DOUBLE NOT NULL,
    current_fill_level DOUBLE NULL,
    type         VARCHAR(255) NULL,
    status       VARCHAR(255) NULL,
    installed_at datetime     NOT NULL,
    location_id  BIGINT NULL,
    CONSTRAINT pk_container PRIMARY KEY (id)
);

CREATE TABLE location
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    address       VARCHAR(255) NULL,
    city          VARCHAR(255) NULL,
    postal_code   VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE measurement
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    sensor_id    BIGINT   NOT NULL,
    container_id BIGINT   NOT NULL,
    fill_level DOUBLE NOT NULL,
    temperature DOUBLE NULL,
    humidity DOUBLE NULL,
    measured_at  datetime NOT NULL,
    CONSTRAINT pk_measurement PRIMARY KEY (id)
);

CREATE TABLE sensor
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    sensor_id        VARCHAR(255) NOT NULL,
    model            VARCHAR(255) NOT NULL,
    type             VARCHAR(255) NULL,
    status           VARCHAR(255) NULL,
    battery_level DOUBLE NULL,
    installed_at     datetime     NOT NULL,
    last_maintenance datetime NULL,
    container_id     BIGINT NULL,
    CONSTRAINT pk_sensor PRIMARY KEY (id)
);

ALTER TABLE container
    ADD CONSTRAINT uc_container_containerid UNIQUE (container_id);

ALTER TABLE container
    ADD CONSTRAINT uc_container_location UNIQUE (location_id);

ALTER TABLE sensor
    ADD CONSTRAINT uc_sensor_sensorid UNIQUE (sensor_id);

ALTER TABLE container
    ADD CONSTRAINT FK_CONTAINER_ON_LOCATION FOREIGN KEY (location_id) REFERENCES location (id);

ALTER TABLE measurement
    ADD CONSTRAINT FK_MEASUREMENT_ON_CONTAINER FOREIGN KEY (container_id) REFERENCES container (id);

ALTER TABLE measurement
    ADD CONSTRAINT FK_MEASUREMENT_ON_SENSOR FOREIGN KEY (sensor_id) REFERENCES sensor (id);

ALTER TABLE sensor
    ADD CONSTRAINT FK_SENSOR_ON_CONTAINER FOREIGN KEY (container_id) REFERENCES container (id);