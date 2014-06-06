CREATE TABLE `configuration` (
  `operation` varchar(45) NOT NULL,
  `status` bit(1) NOT NULL DEFAULT b'0',
  `artists` bit(1) NOT NULL DEFAULT b'0',
  `categories_of_artists` bit(1) NOT NULL DEFAULT b'0',
  `artist_category` bit(1) NOT NULL DEFAULT b'0',
  `songs` bit(1) NOT NULL DEFAULT b'0',
  `categories_of_songs` bit(1) NOT NULL DEFAULT b'0',
  `song_category` bit(1) NOT NULL DEFAULT b'0',
  `artist_song_temp` bit(1) NOT NULL DEFAULT b'0',
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
  `artist_song_temp_distinct` bit(1) NOT NULL DEFAULT b'0',
  `unknown_song_id` bit(1) NOT NULL DEFAULT b'0',
  `update_artist_song_id` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`operation`),
  UNIQUE KEY `operation_UNIQUE` (`operation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `configuration` (`operation`) VALUES ('general');
INSERT INTO `configuration` (`operation`) VALUES ('updateOp');


CREATE TABLE `artist_category` (
  `artist_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`artist_id`,`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='arsist_category(artist_id, category_id)';


CREATE TABLE `artist_song` (
  `song_id` int(11) NOT NULL,
  `artist_id` int(11) NOT NULL,
  PRIMARY KEY (`song_id`,`artist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='artist_song(song_id,artist_id )';


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


CREATE TABLE `song_category` (
  `song_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`song_id`,`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='songCategory(song_id, category_id)';


CREATE TABLE `songs` (
  `song_id` int(11) NOT NULL AUTO_INCREMENT,
  `song_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`song_id`),
UNIQUE INDEX `song_name_UNIQUE` (`song_name` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='songs(song_id, song_name)';


CREATE TABLE `user_artist` (
  `user_id` int(11) NOT NULL,
  `artist_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`artist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='userArtist(user_id,artist_id)';


CREATE TABLE `user_songs` (
  `user_id` int(11) NOT NULL,
  `song_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`song_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='userSongs(user_id , song_id)';


CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `status_song_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='users(user_id, user_name , password , status_song_id)';

CREATE TABLE `users_freinds` (
  `user_id` int(11) NOT NULL,
  `user_freind_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`user_freind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='users_freinds(user_id,user_freind_id)';


