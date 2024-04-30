package com.stats.lolgg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;

@Mapper
public interface LeagueMapper {
    List<LeagueVO> findAll();
    List<LeagueVO> findTopTen(String riot_name);
    List<LeagueStatsVO> findRecord(String riot_name);
    LeagueStatsVO findRecordMonth(String riot_name);
    List<LeagueStatsVO> findMostPick(String riot_name);
    List<LeagueStatsVO> findChampMaster(String champ_name);
    List<LeagueStatsVO> findChampHighRate();
    List<LeagueStatsVO> findRecordWithTeam(String riot_name);
    List<LeagueStatsVO> findRecordOtherTeam(String riot_name);

    void insertLeague(List<LeagueVO> leagueVO);

    int changeDeleteYN(Map<String,Object> paramMap);
    int changeRiotName(Map<String,Object> paramMap);
    
}