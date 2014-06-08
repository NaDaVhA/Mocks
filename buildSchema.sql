CREATE TABLE `configuration` (
  `operation` varchar(45) NOT NULL,
  `status` bit(1) NOT NULL DEFAULT b'0',
  `artists` bit(1) NOT NULL DEFAULT b'0',
  `categories_of_artists` bit(1) NOT NULL DEFAULT b'0',
  `artist_category` bit(1) NOT NULL DEFAULT b'0',
  `songs` bit(1) NOT NULL DEFAULT b'0',
  `categories_of_songs` bit(1) NOT NULL DEFAULT b'0',
  `song_category` bit(1) NOT NULL DEFAULT b'0',
  `artist_song` bit(1) NOT NULL DEFAULT b'0',
  `artist_song_additions` bit(1) NOT NULL DEFAULT b'0',
  `artistsTables` bit(1) NOT NULL DEFAULT b'0',
  `songsTables` bit(1) NOT NULL DEFAULT b'0',
  `creatorCreationTables` bit(1) NOT NULL DEFAULT b'0',
  `artists_temp` bit(1) NOT NULL DEFAULT b'0',
  `artists_temp_after_singers` bit(1) NOT NULL DEFAULT b'0',
  `artist_category_temp` bit(1) NOT NULL DEFAULT b'0',
  `song_category_temp` bit(1) NOT NULL DEFAULT b'0',
  `song_category_distinct` bit(1) NOT NULL DEFAULT b'0',
  `artist_song_temp` bit(1) NOT NULL DEFAULT b'0',
  `artist_song_temp_distinct` bit(1) NOT NULL DEFAULT b'0',
  `unknown_song_id` bit(1) NOT NULL DEFAULT b'0',
  `update_artist_song_id` bit(1) NOT NULL DEFAULT b'0',
  `update_artists_temp` bit(1) NOT NULL DEFAULT b'0',
  `update_artists_distinct` bit(1) NOT NULL DEFAULT b'0',
  `update_artists_categories_distinct` bit(1) NOT NULL DEFAULT b'0',
  `update_artist_category_temp` bit(1) NOT NULL DEFAULT b'0',
  `update_artist_category_id_temp` bit(1) NOT NULL DEFAULT b'0',
  `update_song_category_distinct` bit(1) NOT NULL DEFAULT b'0',
  `update_song_category_temp` bit(1) NOT NULL DEFAULT b'0',
  `update_categories_of_songs_distinct` bit(1) NOT NULL DEFAULT b'0',
  `update_song_category_id` bit(1) NOT NULL DEFAULT b'0',
  `update_artist_song_temp` bit(1) NOT NULL DEFAULT b'0',
  `update_artist_song_distinct` bit(1) NOT NULL DEFAULT b'0',
  `update_artist_song_id_update` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`operation`),
  UNIQUE KEY `operation_UNIQUE` (`operation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#mira

INSERT INTO `configuration` (`operation`) VALUES ('general');
INSERT INTO `configuration` (`operation`) VALUES ('updateOp');



CREATE TABLE `artists` (
   `artist_id` int(11) NOT NULL AUTO_INCREMENT,
   `artist_name` varchar(100) DEFAULT NULL,
   `artist_type` BIT NULL,
   PRIMARY KEY (`artist_id`),
 UNIQUE INDEX `artist_name_UNIQUE` (`artist_name` ASC)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='artists(artist_id, artist_name, artist_type)';


CREATE TABLE `categories_of_artists` (
  `category_artist_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`category_artist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='categories_of_artists(category_artist_id, category_name )';


CREATE TABLE `categories_of_songs` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
UNIQUE INDEX `category_name_UNIQUE` (`category_name` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='categories_of_songs(category_id, category_name )';

CREATE TABLE `songs` (
  `song_id` int(11) NOT NULL AUTO_INCREMENT,
  `song_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`song_id`),
UNIQUE INDEX `song_name_UNIQUE` (`song_name` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='songs(song_id, song_name)';


CREATE TABLE `users` (
`user_id` int(11) NOT NULL AUTO_INCREMENT,
 `user_name` varchar(45) DEFAULT NULL,
 `password` varchar(45) DEFAULT NULL,
 `status_song_id` int(11) DEFAULT NULL,
 `status_artist_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC),
    CONSTRAINT `status_song`
    FOREIGN KEY (`status_song_id`)
    REFERENCES `songs` (`song_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `status_artist`
    FOREIGN KEY (`status_artist_id`)
    REFERENCES `artists` (`artist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE `artist_category` (
  `artist_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`artist_id`,`category_id`),
CONSTRAINT `artist_id_artist_category`
    FOREIGN KEY (`artist_id`)
    REFERENCES `artists` (`artist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `category_id_artist_category`
    FOREIGN KEY (`category_id`)
    REFERENCES `categories_of_artists` (`category_artist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
 ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='arsist_category(artist_id, category_id)';


CREATE TABLE `artist_song` (
  `song_id` int(11) NOT NULL,
  `artist_id` int(11) NOT NULL,
  PRIMARY KEY (`song_id`,`artist_id`),
CONSTRAINT `song_id_artist_song`
    FOREIGN KEY (`song_id`)
    REFERENCES `songs` (`song_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `artist_id_artist_song`
    FOREIGN KEY (`artist_id`)
    REFERENCES `artists` (`artist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='artist_song(song_id,artist_id )';





CREATE TABLE `song_category` (
  `song_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`song_id`,`category_id`),
CONSTRAINT `song_id_song_category`
    FOREIGN KEY (`song_id`)
    REFERENCES `songs` (`song_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `category_id_song_category`
    FOREIGN KEY (`category_id`)
    REFERENCES `categories_of_songs` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='songCategory(song_id, category_id)';




CREATE TABLE `user_artist` (
  `user_id` int(11) NOT NULL,
  `artist_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`artist_id`),

CONSTRAINT `user_id_user_artist`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `artist_id_user_artist`
    FOREIGN KEY (`artist_id`)
    REFERENCES `artists` (`artist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='userArtist(user_id,artist_id)';


CREATE TABLE `users_friends` (
  `user_id` INT NOT NULL,
  `user_friend_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `user_friend_id`),
  CONSTRAINT `user_id_users_friend`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_friend_id_users_friend`
    FOREIGN KEY (`user_friend_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `user_songs` (
  `user_id` INT NOT NULL,
  `song_id` INT NOT NULL,
  `artist_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `song_id`, `artist_id`),
  CONSTRAINT `user_id_user_songs`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `song_id_user_songs`
    FOREIGN KEY (`song_id`)
    REFERENCES `songs` (`song_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `artist_id_user_songs`
    FOREIGN KEY (`artist_id`)
    REFERENCES `artists` (`artist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);