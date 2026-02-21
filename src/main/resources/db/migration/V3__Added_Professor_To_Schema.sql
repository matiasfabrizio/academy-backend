CREATE TABLE professor
(
    id        UUID         NOT NULL,
    name      VARCHAR(255) NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    CONSTRAINT pk_professor PRIMARY KEY (id)
);

ALTER TABLE course
    ADD professor_id UUID;

ALTER TABLE course
    ADD CONSTRAINT FK_COURSE_ON_PROFESSOR FOREIGN KEY (professor_id) REFERENCES professor (id);

ALTER TABLE course
DROP
COLUMN professor;

ALTER TABLE bank_account
ALTER
COLUMN name TYPE VARCHAR(255) USING (name::VARCHAR(255));