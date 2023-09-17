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
--rollback DROP TABLE table2;