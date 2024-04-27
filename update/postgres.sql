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

CREATE SEQUENCE league_id_seq
START WITH 1
INCREMENT BY 1

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
    COUNT(CASE WHEN game_result = '패' THEN 1 END) AS lose,
    ROUND(COUNT(CASE WHEN game_result = '승' THEN 1 END)::numeric / COUNT(*)*100,2) AS win_rate
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
group by champ_name
ORDER BY TOTAL_COUNT DESC
limit 10

/* 픽 순 */
select champ_name , count(champ_name),
		COUNT(CASE WHEN game_result = '승' THEN 1 END) AS win,
		COUNT(CASE WHEN game_result = '패' THEN 1 END) AS lose,
		ROUND(COUNT(CASE WHEN game_result = '승' THEN 1 END)::numeric / COUNT(*)*100,2) AS win_rate

from league 
where delete_yn  = 'N'
group by champ_name
order by win_rate desc , count desc 

/* 장인 */
select 
	riot_name, 
	count(riot_name) as total_count,
	COUNT(CASE WHEN game_result = '승' THEN 1 END) AS win,
	COUNT(CASE WHEN game_result = '패' THEN 1 END) AS lose,
	ROUND(COUNT(CASE WHEN game_result = '승' THEN 1 END)::numeric / COUNT(*)*100,2) AS win_rate
from league 
where champ_name = 'Vi'
and delete_yn = 'N'
group by riot_name 
order by total_count desc  , win_rate  desc

/* game_id */		
select 
	game_id, riot_name, champ_name, position, kda, game_result, game_team, game_date
from 
league l where game_id ='2t_0424_2355'		
ORDER BY 
  CASE WHEN game_team = 'blue' THEN
    CASE position
      WHEN 'TOP' THEN 1
      WHEN 'JUG' THEN 2
      WHEN 'MID' THEN 3
      WHEN 'ADC' THEN 4
      WHEN 'SUP' THEN 5
    END
  ELSE
    CASE position
      WHEN 'TOP' THEN 6
      WHEN 'JUG' THEN 7
      WHEN 'MID' THEN 8
      WHEN 'ADC' THEN 9
      WHEN 'SUP' THEN 10
    END
  END


/* 최근 30게임에서 같은 팀 시너지 */
select 
	A.riot_name,
	count(A.riot_name) as total_count,
	count(CASE WHEN A.game_result = '승' THEN 1 END) as win,
	count(CASE WHEN A.game_result = '패' THEN 1 END) as lose
	from league A 
	inner join 
	(
		select * from league 
		where riot_name = '잘생긴정현이'
		order by game_date desc
		limit 30
	) B
	on A.game_team = B.game_team 
  and A.game_id = B.game_id 
  and A.riot_name != '잘생긴정현이' 
  and A.delete_yn = 'N'
	group by A.riot_name
	having  COUNT(A.riot_name) >= 3
	order by total_count desc

/* 나와 인간상성 찾기 */
select 
	A.riot_name,
	count(A.riot_name) as total_count,
	count(CASE WHEN A.game_result = '승' THEN 1 END) as win,
	count(CASE WHEN A.game_result = '패' THEN 1 END) as lose
	from league A 
	inner join 
	(
		select * from league 
		where riot_name = '신조차모독한천재'
		order by game_date desc
		limit 30
	) B
	on A.game_team != B.game_team 
  and A.game_id = B.game_id 
  and A.riot_name != '신조차모독한천재' 
  and A.delete_yn = 'N'
	and A.position = B.position
	group by A.riot_name
	having  COUNT(A.riot_name) >= 3
	order by total_count desc
