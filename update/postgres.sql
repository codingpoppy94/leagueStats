create database lolgg;
create user rnasyd password 'welcome1!' SUPERUSER;
drop user rnasyd;

CREATE TABLE league (
    id VARCHAR(20) PRIMARY KEY,
    game_id VARCHAR(20),
    riot_name VARCHAR(200),
    champ_name VARCHAR(200),
    game_line VARCHAR(100),
    kda VARCHAR(20),
    game_result boolean not null,
    game_team VARCHAR(20),
    game_date TIMESTAMP,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);