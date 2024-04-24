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


-- 최근전적 10 조회
SELECT id, game_id, riot_name, champ_name, position, kda, game_result, game_team, game_date, create_date
FROM league

where riot_name = '잘생긴정현이'
and delete_yn = 'N'
and game_date < current_timestamp 
ORDER BY ABS(EXTRACT(EPOCH FROM CURRENT_TIMESTAMP - game_date))
LIMIT 10;

-- 전체 조회
SELECT id, game_id, riot_name, champ_name, position, kda, game_result, game_team, game_date, create_date
FROM league

where riot_name = '잘생긴정현이'
and delete_yn = 'N';

-- 이번달 조회
SELECT id, game_id, riot_name, champ_name, position, kda, game_result, game_team, game_date, create_date
FROM league
where riot_name = '잘생긴정현이'
and delete_yn  = 'N'
and game_date >= DATE_TRUNC('month', CURRENT_TIMESTAMP)
and game_date < DATE_TRUNC('month', CURRENT_TIMESTAMP) + INTERVAL '1 month';

-- 전적 count
select
	position ,
    COUNT(*) AS total_count,
    COUNT(CASE WHEN game_result = '승' THEN 1 END) AS win,
    COUNT(CASE WHEN game_result = '패' THEN 1 END) AS lose

FROM league
where riot_name = '잘생긴정현이'
and delete_yn  = 'N'
and game_date >= DATE_TRUNC('month', CURRENT_TIMESTAMP)
and game_date < DATE_TRUNC('month', CURRENT_TIMESTAMP) + INTERVAL '1 month'

group by position;


-- 모스트픽
SELECT champ_name,count(champ_name) ,
	    COUNT(CASE WHEN game_result = '승' THEN 1 END) AS win,
	     COUNT(CASE WHEN game_result = '패' THEN 1 END) AS lose
FROM league

where riot_name = '잘생긴정현이'
and delete_yn = 'N'
group by champ_name, game_result