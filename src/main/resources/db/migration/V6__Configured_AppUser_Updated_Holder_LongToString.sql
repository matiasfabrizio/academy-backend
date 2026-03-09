ALTER TABLE app_user
    ADD google_id VARCHAR(255);

ALTER TABLE app_user
    ADD phone_number VARCHAR(255);

ALTER TABLE app_user
    ADD profile_complete BOOLEAN;

ALTER TABLE app_user
    ALTER COLUMN phone_number SET NOT NULL;

ALTER TABLE app_user
    ALTER COLUMN profile_complete SET NOT NULL;

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_dni UNIQUE (dni);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_email UNIQUE (email);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_phonenumber UNIQUE (phone_number);

ALTER TABLE app_user
DROP
COLUMN dni;

ALTER TABLE app_user
    ADD dni VARCHAR(255) NOT NULL;

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_dni UNIQUE (dni);

ALTER TABLE holder
DROP
COLUMN phone_number;

ALTER TABLE holder
    ADD phone_number VARCHAR(255) NOT NULL;