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