--liquibase formatted sql



--changeset bogdanovtim:1
CREATE TABLE IF NOT EXISTS table1 ( /*? One to Many */
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name       VARCHAR (255) NOT NULL UNIQUE,
    enumeration VARCHAR (255),
    PRIMARY KEY (id)
);
--rollback DROP TABLE table1;


--changeset bogdanovtim:2
CREATE TABLE IF NOT EXISTS table2 ( /*? Many to One */
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name       VARCHAR (255) NOT NULL UNIQUE,
    table1_id  BIGINT UNSIGNED,
    enumeration VARCHAR (255),
    FOREIGN KEY (table1_id) REFERENCES table1 (id),
    PRIMARY KEY (id)
);
--rollback DROP TABLE table2;

--changeset bogdanovtim:3
CREATE TABLE IF NOT EXISTS Users
(
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    password   VARCHAR(255) DEFAULT NULL,
    address    VARCHAR(255) DEFAULT NULL,
    phone      VARCHAR(30) DEFAULT NULL,
    title      VARCHAR(50) DEFAULT NULL,
    bio        VARCHAR(255) DEFAULT NULL,
    enabled    BOOLEAN DEFAULT FALSE,
    non_locked BOOLEAN DEFAULT TRUE,
    using_mfa  BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    image_url  VARCHAR(255) DEFAULT 'https://cdn-icons-png.flaticon.com/512/149/149071.png',
    CONSTRAINT UQ_Users_Email UNIQUE (email)
);



--changeset bogdanovtim:4
CREATE TABLE IF NOT EXISTS Roles
(
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    permission VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_Roles_Name UNIQUE (name)
);

--rollback DROP TABLE IF EXISTS Roles;



--changeset bogdanovtim:5
CREATE TABLE IF NOT EXISTS UserRoles
(
    id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    role_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Roles (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_UserRoles_User_Id UNIQUE (user_id)
);

--rollback DROP TABLE IF EXISTS UserRoles;



--changeset bogdanovtim:6
CREATE TABLE IF NOT EXISTS Events
(
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type        VARCHAR(50) NOT NULL CHECK(type IN ('LOGIN_ATTEMPT', 
                                                    'LOGIN_ATTEMPT_FAILURE',
                                                    'LOGIN_ATTEMPT_SUCCESS',
                                                    'PROFILE_UPDATE',
                                                    'PROFILE_PICTURE_UPDATE',
                                                    'ROLE_UPDATE',
                                                    'ACCOUNT_SETTINGS_UPDATE',
                                                    'PASSWORD_UPDATE',
                                                    'MFA_UPDATE')),
    description VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_Events_Type UNIQUE (type)
);

--rollback DROP TABLE IF EXISTS Events;



--changeset bogdanovtim:7
CREATE TABLE IF NOT EXISTS UserEvents
(
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT UNSIGNED NOT NULL,
    event_id   BIGINT UNSIGNED NOT NULL,
    device     VARCHAR(100) DEFAULT NULL,
    ip_address VARCHAR(100) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Events (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

--rollback DROP TABLE IF EXISTS UserEvents;



--changeset bogdanovtim:8
CREATE TABLE IF NOT EXISTS AccountVerifications
(
    id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    url     VARCHAR(255) NOT NULL,
    -- date     DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_AccountVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_AccountVerifications_Url UNIQUE (url)
);

--rollback DROP TABLE IF EXISTS AccountVerifications;



--changeset bogdanovtim:9
CREATE TABLE IF NOT EXISTS ResetPasswordVerifications
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT UNSIGNED NOT NULL,
    url             VARCHAR(255) NOT NULL,
    expiration_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_ResetPasswordVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_ResetPasswordVerifications_Url UNIQUE (url)
);

--rollback DROP TABLE IF EXISTS ResetPasswordVerifications;



--changeset bogdanovtim:10
CREATE TABLE IF NOT EXISTS TwoFactorVerifications
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT UNSIGNED NOT NULL,
    code            VARCHAR(10) NOT NULL,
    expiration_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_TwoFactorVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_TwoFactorVerifications_Code UNIQUE (code)
);

--rollback DROP TABLE IF EXISTS TwoFactorVerifications;