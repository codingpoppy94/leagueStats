create database lolgg;
create user rnasyd password 'welcome1!' SUPERUSER;
drop user rnasyd;

CREATE TABLE league (
    id VARCHAR(20) PRIMARY KEY,
    game_id VARCHAR(20),
    riot_name VARCHAR(200),
    champ_name VARCHAR(200),
    position VARCHAR(100),
    kda VARCHAR(20),
    game_result VARCHAR(20),
    game_team VARCHAR(20),
    game_date TIMESTAMP,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete_yn char(1)
);

INSERT INTO public.league
(id, game_id, riot_name, champ_name, position, kda, game_result, game_team, game_date, create_date, delete_yn)
VALUES('1', '03-28-1-1', '잘정', '자르반4세', 'JUG', '10/1/13', "승", 'blue', '20240328', CURRENT_TIMESTAMP, 'N');
