package com.stats.lolgg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.stats.lolgg.model.LeagueVO;

@Mapper
public interface LeagueMapper {
    List<LeagueVO> findAll();
    LeagueVO findByRiotName(String riotName);
    void insertLeague(List<LeagueVO> leagueVO);
    
}