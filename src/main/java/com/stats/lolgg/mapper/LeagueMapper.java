package com.stats.lolgg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.stats.lolgg.model.ChampMasterVO;
import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;

/**
 * League Mapper
 * @author codingpoppy94
 * @version 1.0
 */
@Mapper
public interface LeagueMapper {
    List<LeagueVO> findAll();
    List<LeagueVO> findTopTen(String riot_name);
    List<LeagueStatsVO> findRecord(String riot_name);
    LeagueStatsVO findRecordMonth(String riot_name);
    List<LeagueStatsVO> findMostPick(String riot_name);
    List<ChampMasterVO> findChampMaster(String champ_name);
    List<LeagueStatsVO> findChampStats(int year,int month);
    List<LeagueStatsVO> findRecordWithTeam(String riot_name);
    List<LeagueStatsVO> findRecordOtherTeam(String riot_name);
    List<LeagueStatsVO> findRecordLine(String position);
    List<Map<String,Object>> findMappingName();
    int findReplayName(String game_id);

    List<LeagueStatsVO> groupLeagueByRiotName(int year,int month);

    void insertLeague(List<LeagueVO> leagueVO);
    void insertMappingName(Map<String,Object> paramMap);

    int changeDeleteYN(Map<String,Object> paramMap);
    int changeMappingDeleteYN(Map<String,Object> paramMap);
    int changeRiotName(Map<String,Object> paramMap);
    
}