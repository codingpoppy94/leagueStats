create database lolgg;
create user rnasyd password 'welcome1!' SUPERUSER;
drop user rnasyd;

CREATE TABLE league (
    id VARCHAR(20) PRIMARY KEY,
    game_id VARCHAR(20),
    riot_name VARCHAR(200),
    champ_name VARCHAR(200),
    position VARCHAR(100),
    kill Integer,
    death Integer,
    assist Integer,
    game_result VARCHAR(20),
    game_team VARCHAR(20),
    game_date TIMESTAMP,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete_yn char(1),
    create_user VARCHAR(20)
);

create table mapping_name (
	id varchar(20) primary key,
	sub_name varchar(200),
	main_name varchar(200),
	create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  delete_yn char(1)
);

CREATE SEQUENCE mapping_name_id_seq
START WITH 1
INCREMENT BY 1

CREATE SEQUENCE league_id_seq
START WITH 1
INCREMENT BY 1

-- 최근전적 10 조회
SELECT id, game_id, riot_name, champ_name, position, kill, death, assist, game_result, game_team, game_date, create_date
FROM league

where riot_name = '잘생긴정현이'
and delete_yn = 'N'
and game_date < current_timestamp 
ORDER BY ABS(EXTRACT(EPOCH FROM CURRENT_TIMESTAMP - game_date))
LIMIT 10;

-- 전체 조회
SELECT id, game_id, riot_name, champ_name, position, kill, death, assist, game_result, game_team, game_date, create_date
FROM league

where riot_name = '잘생긴정현이'
and delete_yn = 'N';

-- 이번달 조회
SELECT id, game_id, riot_name, champ_name, position, kill, death, assist, game_result, game_team, game_date, create_date
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

/* 장인 - 수정예정 */
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
	game_id, riot_name, champ_name, position, kill, death, assist, game_result, game_team, game_date
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
	count(CASE WHEN B.game_result = '승' THEN 1 END) as win,
	count(CASE WHEN B.game_result = '패' THEN 1 END) as lose,
	ROUND(COUNT(CASE WHEN B.game_result = '승' THEN 1 END)::numeric / COUNT(*)*100,2) AS win_rate
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
	order by win_rate desc

/* 나와 인간상성 찾기 */
select 
	A.riot_name,
	count(A.riot_name) as total_count,
	count(CASE WHEN B.game_result = '승' THEN 1 END) as win,
	count(CASE WHEN B.game_result = '패' THEN 1 END) as lose,
	ROUND(COUNT(CASE WHEN B.game_result = '승' THEN 1 END)::numeric / COUNT(*)*100,2) AS win_rate
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
--	having  COUNT(A.riot_name) >= 3
	order by win_rate desc

/* 라인별 승률  */
select 	position,
		riot_name,
		count(riot_name) as total_count,
		count(CASE WHEN game_result = '승' THEN 1 END) as win,
		count(CASE WHEN game_result = '패' THEN 1 END) as lose,
		ROUND(COUNT(CASE WHEN game_result = '승' THEN 1 END)::numeric / COUNT(*)*100,2) AS win_rate,
		 CASE
			WHEN SUM(DEATH) = 0 THEN 9999
			ELSE ROUND((SUM(KILL) + SUM(ASSIST))::NUMERIC / NULLIF(SUM(DEATH), 0), 2) 
        END AS KDA
from league  
where position = 'TOP'
group by "position" ,riot_name 
having  COUNT(riot_name) >= 5
order by total_count desc, win_rate desc
limit 15    

/* 통계 */
select 
	CHAMP_NAME,
    COUNT(CHAMP_NAME) AS TOTAL_COUNT,
    COUNT(CASE WHEN game_result = '승' THEN 1 END) AS win,
    COUNT(CASE WHEN game_result = '패' THEN 1 END) AS lose,
    ROUND(COUNT(CASE WHEN game_result = '승' THEN 1 END)::numeric / COUNT(*)*100,2) AS win_rate
from league 
where delete_yn  = 'N'
-- 이번달
AND GAME_DATE >= DATE_TRUNC('month', CURRENT_TIMESTAMP)
AND GAME_DATE < DATE_TRUNC('month', CURRENT_TIMESTAMP) + INTERVAL '1 month'
-- 저번달
-- AND GAME_DATE >= DATE_TRUNC('month', CURRENT_TIMESTAMP - INTERVAL '1 month')
--AND GAME_DATE < DATE_TRUNC('month', CURRENT_TIMESTAMP)
group by champ_name
HAVING COUNT(CHAMP_NAME) >= 20

/* 블루vs레드*/
select 
	game_team,
	count(game_team) as total_count,
    COUNT(CASE WHEN game_result = '승' THEN 1 END) AS win,
    COUNT(CASE WHEN game_result = '패' THEN 1 END) AS lose,
ROUND(COUNT(CASE WHEN game_result = '승' THEN 1 END)::numeric / COUNT(*)*100,2) AS win_rate
from league
where delete_yn  = 'N'
AND GAME_DATE >= DATE_TRUNC('month', CURRENT_TIMESTAMP)
AND GAME_DATE < DATE_TRUNC('month', CURRENT_TIMESTAMP) + INTERVAL '1 month'
group by game_team

/* 한글 update */
UPDATE league
SET champ_name = 
CASE
    WHEN champ_name = 'Aatrox' THEN '아트록스'
    WHEN champ_name = 'Ahri' THEN '아리'
    WHEN champ_name = 'Akali' THEN '아칼리'
    WHEN champ_name = 'Akshan' THEN '아크샨'
    WHEN champ_name = 'Alistar' THEN '알리스타'
    WHEN champ_name = 'Amumu' THEN '아무무'
    WHEN champ_name = 'Annie' THEN '애니'
    WHEN champ_name = 'Anivia' THEN '애니비아'
    WHEN champ_name = 'Aphelios' THEN '아펠리오스'
    WHEN champ_name = 'Ashe' THEN '애쉬'
    WHEN champ_name = 'AurelionSol' THEN '아우렐리온솔'
    WHEN champ_name = 'Azir' THEN '아지르'
    WHEN champ_name = 'Bard' THEN '바드'
    WHEN champ_name = 'Belveth' THEN '벨베스'
    WHEN champ_name = 'Blitzcrank' THEN '블리츠크랭크'
    WHEN champ_name = 'Brand' THEN '브랜드'
    WHEN champ_name = 'Braum' THEN '브라움'
    WHEN champ_name = 'Briar' THEN '브라이어'
    WHEN champ_name = 'Caitlyn' THEN '케이틀린'
    WHEN champ_name = 'Camille' THEN '카밀'
    WHEN champ_name = 'Cassiopeia' THEN '카시오페아'
    WHEN champ_name = 'Chogath' THEN '초가스'
    WHEN champ_name = 'Corki' THEN '코르키'
    WHEN champ_name = 'Darius' THEN '다리우스'
    WHEN champ_name = 'Diana' THEN '다이애나'
    WHEN champ_name = 'Draven' THEN '드레이븐'
    WHEN champ_name = 'DrMundo' THEN '문도박사'
    WHEN champ_name = 'Ekko' THEN '에코'
    WHEN champ_name = 'Elise' THEN '엘리스'
    WHEN champ_name = 'Evelynn' THEN '이블린'
    WHEN champ_name = 'Ezreal' THEN '이즈리얼'
    WHEN champ_name = 'Fiddlesticks' THEN '피들스틱'
    WHEN champ_name = 'Fiora' THEN '피오라'
    WHEN champ_name = 'Fizz' THEN '피즈'
    WHEN champ_name = 'Galio' THEN '갈리오'
    WHEN champ_name = 'Garen' THEN '가렌'
    WHEN champ_name = 'Gnar' THEN '나르'
    WHEN champ_name = 'Gragas' THEN '그라가스'
    WHEN champ_name = 'Graves' THEN '그레이브즈'
    WHEN champ_name = 'Gwen' THEN '그웬'
    WHEN champ_name = 'Hecarim' THEN '헤카림'
    WHEN champ_name = 'Heimerdinger' THEN '하이머딩거'
    WHEN champ_name = 'Hwei' THEN '흐웨이'
    WHEN champ_name = 'Illaoi' THEN '일라오이'
    WHEN champ_name = 'Irelia' THEN '이렐리아'
    WHEN champ_name = 'Ivern' THEN '아이번'
    WHEN champ_name = 'Janna' THEN '잔나'
    WHEN champ_name = 'JarvanIV' THEN '자르반4세'
    WHEN champ_name = 'Jax' THEN '잭스'
    WHEN champ_name = 'Jayce' THEN '제이스'
    WHEN champ_name = 'Jhin' THEN '진'
    WHEN champ_name = 'Jinx' THEN '징크스'
    WHEN champ_name = 'Kaisa' THEN '카이사'
    WHEN champ_name = 'Kalista' THEN '칼리스타'
    WHEN champ_name = 'Karma' THEN '카르마'
    WHEN champ_name = 'Karthus' THEN '카서스'
    WHEN champ_name = 'Kassadin' THEN '카사딘'
    WHEN champ_name = 'Katarina' THEN '카타리나'
    WHEN champ_name = 'Kayle' THEN '케일'
    WHEN champ_name = 'Kayn' THEN '케인'
    WHEN champ_name = 'Kennen' THEN '케넨'
    WHEN champ_name = 'Khazix' THEN '카직스'
    WHEN champ_name = 'Kindred' THEN '킨드레드'
    WHEN champ_name = 'Kled' THEN '클레드'
    WHEN champ_name = 'KogMaw' THEN '코그모'
    WHEN champ_name = 'KSante' THEN '크산테'
    WHEN champ_name = 'Leblanc' THEN '르블랑'
    WHEN champ_name = 'LeeSin' THEN '리신'
    WHEN champ_name = 'Leona' THEN '레오나'
    WHEN champ_name = 'Lillia' THEN '릴리아'
    WHEN champ_name = 'Lissandra' THEN '리산드라'
    WHEN champ_name = 'Lucian' THEN '루시안'
    WHEN champ_name = 'Lulu' THEN '룰루'
    WHEN champ_name = 'Lux' THEN '럭스'
    WHEN champ_name = 'Malphite' THEN '말파이트'
    WHEN champ_name = 'Maokai' THEN '마오카이'
    WHEN champ_name = 'MissFortune' THEN '미스포츈'
    WHEN champ_name = 'MonkeyKing' THEN '오공'
    WHEN champ_name = 'Mordekaiser' THEN '모데카이저'
    WHEN champ_name = 'Morgana' THEN '모르가나'
    WHEN champ_name = 'MasterYi' THEN '마스터이'
    WHEN champ_name = 'Malzahar' THEN '말자하'
    WHEN champ_name = 'Milio' THEN '밀리오'
    WHEN champ_name = 'Nami' THEN '나미'
    WHEN champ_name = 'Nasus' THEN '나서스'
    WHEN champ_name = 'Naafiri' THEN '나피리'
    WHEN champ_name = 'Nautilus' THEN '노틸러스'
    WHEN champ_name = 'Neeko' THEN '니코'
    WHEN champ_name = 'Nidalee' THEN '니달리'
    WHEN champ_name = 'Nilah' THEN '닐라'
    WHEN champ_name = 'Nocturne' THEN '녹턴'
    WHEN champ_name = 'Olaf' THEN '올라프'
    WHEN champ_name = 'Orianna' THEN '오리아나'
    WHEN champ_name = 'Ornn' THEN '오른'
    WHEN champ_name = 'Pantheon' THEN '판테온'
    WHEN champ_name = 'Poppy' THEN '뽀삐'
    WHEN champ_name = 'Pyke' THEN '파이크'
    WHEN champ_name = 'Qiyana' THEN '키아나'
    WHEN champ_name = 'Quinn' THEN '퀸'
    WHEN champ_name = 'Rakan' THEN '라칸'
    WHEN champ_name = 'Rammus' THEN '람머스'
    WHEN champ_name = 'RekSai' THEN '렉사이'
    when champ_name = 'Renekton' then '레넥톤'
    when champ_name = 'Rengar' then '렝가'
    when champ_name = 'Rumble' then '럼블'
    when champ_name = 'Ryze' then '라이즈'
    WHEN champ_name = 'Rell' THEN '렐'
    WHEN champ_name = 'Renata' THEN '레나타'
    WHEN champ_name = 'Riven' THEN '리븐'
    WHEN champ_name = 'Samira' THEN '사미라'
    WHEN champ_name = 'Sejuani' THEN '세주아니'
    WHEN champ_name = 'Senna' THEN '세나'
    WHEN champ_name = 'Seraphine' THEN '세라핀'
    WHEN champ_name = 'Sett' THEN '세트'
    WHEN champ_name = 'Shaco' THEN '샤코'
    WHEN champ_name = 'Shen' THEN '쉔'
    WHEN champ_name = 'Shyvana' THEN '쉬바나'
    WHEN champ_name = 'Singed' THEN '신지드'
    WHEN champ_name = 'Sion' THEN '사이온'
    WHEN champ_name = 'Sivir' THEN '시비르'
    WHEN champ_name = 'Skarner' THEN '스카너'
    WHEN champ_name = 'Smolder' THEN '스몰더'
    WHEN champ_name = 'Sona' THEN '소나'
    WHEN champ_name = 'Soraka' THEN '소라카'
    WHEN champ_name = 'Swain' THEN '스웨인'
    WHEN champ_name = 'Sylas' THEN '사일러스'
    WHEN champ_name = 'Syndra' THEN '신드라'
    WHEN champ_name = 'TahmKench' THEN '탐켄치'
    WHEN champ_name = 'Taliyah' THEN '탈리야'
    WHEN champ_name = 'Talon' THEN '탈론'
    WHEN champ_name = 'Taric' THEN '타릭'
    WHEN champ_name = 'Teemo' THEN '티모'
    WHEN champ_name = 'Thresh' THEN '쓰레쉬'
    WHEN champ_name = 'Tristana' THEN '트리스타나'
    WHEN champ_name = 'Trundle' THEN '트런들'
    WHEN champ_name = 'Tryndamere' THEN '트린다미어'
    WHEN champ_name = 'TwistedFate' THEN '트페'
    WHEN champ_name = 'Twitch' THEN '트위치'
    WHEN champ_name = 'Udyr' THEN '우디르'
    WHEN champ_name = 'Urgot' THEN '우르곳'
    WHEN champ_name = 'Varus' THEN '바루스'
    WHEN champ_name = 'Vayne' THEN '베인'
    WHEN champ_name = 'Veigar' THEN '베이가'
    WHEN champ_name = 'Velkoz' THEN '벨코즈'
    WHEN champ_name = 'Vex' THEN '벡스'
    WHEN champ_name = 'Vi' THEN '바이'
    WHEN champ_name = 'Viego' THEN '비에고'
    WHEN champ_name = 'Viktor' THEN '빅토르'
    WHEN champ_name = 'Volibear' THEN '볼리베어'
    WHEN champ_name = 'Warwick' THEN '워윅'
    WHEN champ_name = 'Xayah' THEN '자야'
    WHEN champ_name = 'Xerath' THEN '제라스'
    WHEN champ_name = 'XinZhao' THEN '신짜오'
    WHEN champ_name = 'Yasuo' THEN '야스오'
    WHEN champ_name = 'Yone' THEN '요네'
    WHEN champ_name = 'Yorick' THEN '요릭'
    WHEN champ_name = 'Yuumi' THEN '유미'
    WHEN champ_name = 'Zac' THEN '자크'
    WHEN champ_name = 'Zed' THEN '제드'
    WHEN champ_name = 'Zeri' THEN '제리'
    WHEN champ_name = 'Ziggs' THEN '직스'
    WHEN champ_name = 'Zilean' THEN '질리언'
    WHEN champ_name = 'Zoe' THEN '조이'
    WHEN champ_name = 'Zyra' THEN '자이라'
    else champ_name
END;