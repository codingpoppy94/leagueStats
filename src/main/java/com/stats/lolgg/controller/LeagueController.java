package com.stats.lolgg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stats.lolgg.mapper.LeagueMapper;
import com.stats.lolgg.model.LeagueVO;


@RestController
@RequestMapping("/league")
public class LeagueController {
    @Autowired
    private final LeagueMapper leagueMapper;

    public LeagueController(LeagueMapper leagueMapper){
        this.leagueMapper = leagueMapper;
    }

    @GetMapping
    public List<LeagueVO> getLeagueList() {
        return leagueMapper.findAll();
    }
    
    
}
