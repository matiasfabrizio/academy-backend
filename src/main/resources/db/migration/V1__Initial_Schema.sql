CREATE TABLE app_user
(
    id         UUID         NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    dni        BIGINT       NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_appuser PRIMARY KEY (id)
);

CREATE TABLE bank_account
(
    id             UUID   NOT NULL,
    holder_id      UUID   NOT NULL,
    account_number BIGINT NOT NULL,
    cci            BIGINT NOT NULL,
    CONSTRAINT pk_bankaccount PRIMARY KEY (id)
);

CREATE TABLE course
(
    id          UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    start_date  date         NOT NULL,
    end_date    date         NOT NULL,
    photo_url   VARCHAR(255) NOT NULL,
    type        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    professor   VARCHAR(255),
    price       DECIMAL(10, 2),
    holder_id   UUID,
    code        VARCHAR(255),
    tag         VARCHAR(255),
    subtitle    VARCHAR(255),
    text_list   JSONB,
    CONSTRAINT pk_course PRIMARY KEY (id)
);

CREATE TABLE enrollment
(
    id              UUID         NOT NULL,
    student_id      UUID         NOT NULL,
    schedule_id     UUID         NOT NULL,
    enrollment_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status          VARCHAR(255) NOT NULL,
    CONSTRAINT pk_enrollment PRIMARY KEY (id)
);

CREATE TABLE holder
(
    id           UUID         NOT NULL,
    name         VARCHAR(255) NOT NULL,
    phone_number BIGINT       NOT NULL,
    CONSTRAINT pk_holder PRIMARY KEY (id)
);

CREATE TABLE schedule
(
    id                UUID         NOT NULL,
    course_id         UUID         NOT NULL,
    capacity          INTEGER      NOT NULL,
    enrolled_students INTEGER      NOT NULL,
    day               VARCHAR(255) NOT NULL,
    start_time        time WITHOUT TIME ZONE NOT NULL,
    end_time          time WITHOUT TIME ZONE NOT NULL,
    mode              VARCHAR(255) NOT NULL,
    class_number      INTEGER      NOT NULL,
    classroom         VARCHAR(255),
    CONSTRAINT pk_schedule PRIMARY KEY (id)
);

ALTER TABLE bank_account
    ADD CONSTRAINT FK_BANKACCOUNT_ON_HOLDER FOREIGN KEY (holder_id) REFERENCES holder (id);

ALTER TABLE course
    ADD CONSTRAINT FK_COURSE_ON_HOLDER FOREIGN KEY (holder_id) REFERENCES holder (id);

ALTER TABLE enrollment
    ADD CONSTRAINT FK_ENROLLMENT_ON_SCHEDULE FOREIGN KEY (schedule_id) REFERENCES schedule (id);

ALTER TABLE enrollment
    ADD CONSTRAINT FK_ENROLLMENT_ON_STUDENT FOREIGN KEY (student_id) REFERENCES app_user (id);

ALTER TABLE schedule
    ADD CONSTRAINT FK_SCHEDULE_ON_COURSE FOREIGN KEY (course_id) REFERENCES course (id);