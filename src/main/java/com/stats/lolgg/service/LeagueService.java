package com.stats.lolgg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stats.lolgg.mapper.LeagueMapper;
import com.stats.lolgg.model.LeagueVO;


@Service
public class LeagueService {

    @Autowired
    private LeagueMapper leagueMapper;
    
    // public LeagueService(LeagueMapper leagueMapper){
    //     this.leagueMapper = leagueMapper;
    // }

    public List<LeagueVO> findAll(){
        return leagueMapper.findAll();
    }

    public List<LeagueVO> saveAll(List<LeagueVO> leagueVO){
        return leagueMapper.insertLeague(leagueVO);
    }
}
