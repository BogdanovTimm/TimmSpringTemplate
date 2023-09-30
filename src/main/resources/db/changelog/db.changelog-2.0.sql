--liquibase formatted sql



--changeset bogdanovtim:1
INSERT INTO table1
       (name                    ) /* Insert nothing into id_column */
       /*-----------------------*/
VALUES ("metallica@gmail.com"   ),
       ("beastcavers@gmail.com" ),
       ("blacksabbath@gmail.com"),
       ("megadeth@gmail.com"    ),
       ("mastodon@gmail.com"    ),
       ("tim@gmail.com"         )
;
--rollback DELETE FROM table1;



--changeset bogdanovtim:2
INSERT INTO table2
       (name                , table1_id) /* Insert nothing into id_column */
       /*-------------------|---------*/
VALUES ("Crack the Sky"     , 5        ),
       ("Leviathan"         , 5        ),
       ("Paranoid"          , 3        ),
       ("My Favorite Things", null     ),
       ("Remission"         , 5        ),
       ("Blood Mountain"    , 5        )

;
--rollback DELETE FROM table2;



--changeset bogdanovtim:3
INSERT INTO Events
       (type                     , description                            ) /* Insert nothing into id_column */
       /*------------------------|----------------------------------------*/
VALUES ("LOGIN_ATTEMPT"          , "You tried to log in"                  ),
       ("LOGIN_ATTEMPT_SUCCESS"  , "You tried to log in and you successed"),
       ("LOGIN_ATTEMPT_FAILURE"  , "You tried to login  and you failed"   ),
       ("PROFILE_UPDATE"         , "You updated your profile information" ),
       ("PROFILE_PICTURE_UPDATE" , "You updated your profile picture"     ),
       ("ROLE_UPDATE"            , "You updated your role and permissions"),
       ("ACCOUNT_SETTINGS_UPDATE", "You updated your account settings"    ),
       ("MFA_UPDATE"             , "You updated your MFA settings"        ),
       ("PASSWORD_UPDATE"        , "You updated your password"            )
;
--rollback DELETE FROM events;