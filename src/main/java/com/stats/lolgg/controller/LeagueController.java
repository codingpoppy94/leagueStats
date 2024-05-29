package com.stats.lolgg.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;
import com.stats.lolgg.service.LeagueService;
import com.stats.lolgg.service.ReplayService;

import lombok.RequiredArgsConstructor;


/**
 * League API
 * @author codingpoppy94
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/league")
public class LeagueController {

    private final LeagueService leagueService;

    private final ReplayService replayService;

    /**
     * 전체 조회
     * @return
     */
    @GetMapping
    public List<LeagueVO> getLeagueList() {
        return leagueService.findAll();
    }

    @GetMapping("/getTopTen")
    public List<LeagueVO> getTopTen(@RequestParam String riot_name){
        
        return leagueService.findTopTen(riot_name);
    }

    @GetMapping("/getRecord")
    public List<LeagueStatsVO> getRecord(@RequestParam String riot_name){
        return leagueService.findRecord(riot_name);
    }

    @GetMapping("/getRecordMonth")
    public LeagueStatsVO getRecordMonth(@RequestParam String riot_name){
        return leagueService.findRecordMonth(riot_name);
    }

    @GetMapping("/getMostPick")
    public List<LeagueStatsVO> getMostPick(@RequestParam String riot_name){
        return leagueService.findMostPick(riot_name);
    }

    @GetMapping("/getChampMaster")
    public List<LeagueStatsVO> getChampMaster(@RequestParam String champ_name){
        return leagueService.findChampMaster(champ_name);
    }

    @PostMapping("/getReplayData")
    public String getReplayData(@RequestBody Map<String,Object> body) throws Exception {
        String fileNameWithExt = (String) body.get("fileNameWithExt");
        String createUser = (String) body.get("createUser");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode statsArray = objectMapper.convertValue(body.get("body"), JsonNode.class);

        replayService.saveFromApi(statsArray, fileNameWithExt, createUser);
        return "성공";
    }
}
