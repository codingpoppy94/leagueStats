package com.stats.lolgg.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;
import com.stats.lolgg.service.LeagueService;
import com.stats.lolgg.service.ParseService;
import com.stats.lolgg.service.ReplayService;

import io.swagger.v3.oas.annotations.Operation;
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

    private final ParseService parseService;

    // /**
    //  * 전체 조회
    //  * @return
    //  */
    // @GetMapping
    // public List<LeagueVO> getLeagueList() {
    //     return leagueService.findAll();
    // }

    @Operation(summary = "최근전적 10개")
    @GetMapping("/getTopTen")
    public List<LeagueVO> getTopTen(@RequestParam String riot_name){
        return leagueService.findTopTen(riot_name);
    }

    // !전적 라인별 통합전적 조회 findRecord
    @Operation(summary = "!전적 라인별 통합전적 조회")
    @GetMapping("/getRecord")
    public List<LeagueStatsVO> getRecord(@RequestParam String riot_name){
        return leagueService.findRecord(riot_name);
    }

    // !전적 이번달 전적 조회 findRecordMonth
    @Operation(summary = "!전적 이번달 전적 조회")
    @GetMapping("/getRecordMonth")
    public LeagueStatsVO getRecordMonth(@RequestParam String riot_name){
        return leagueService.findRecordMonth(riot_name);
    }

    // !전적 Most Pick 10 조회 findMostPick
    @Operation(summary = "!전적 모스트 픽 조회")
    @GetMapping("/getMostPick")
    public List<LeagueStatsVO> getMostPick(@RequestParam String riot_name){
        return leagueService.findMostPick(riot_name);
    }

    // @GetMapping("/getChampMaster")
    // public List<ChampMasterDTO> getChampMaster(@RequestParam String champ_name){
    //     return leagueService.findChampMaster(champ_name);
    // }

    // !전적 팀워크 조회 findRecordWithTeam
    @Operation(summary = "!전적 팀워크 조회")
    @GetMapping("/getRecordWithTeam")
    public List<LeagueStatsVO> getRecordWithTeam(@RequestParam String riot_name){
        return leagueService.findRecordWithTeam(riot_name);
    }

    // !전적 맞라인 조회 findRecordOtherTeam
    @Operation(summary = "!전적 맞라인 조회")
    @GetMapping("/getRecordOtherTeam")
    public List<LeagueStatsVO> getRecordOtherTeam(@RequestParam String riot_name){
        return leagueService.findRecordOtherTeam(riot_name);
    }

    // 14.11 패치 이후
    @PostMapping("/parseFromApi")
    public String multiParse(@RequestBody List<MultipartFile> files) throws IOException, Exception {
        for(MultipartFile file : files) {
            byte[] bytes = parseService.changeByteArray(file.getInputStream());
            JsonNode jsonNode = parseService.parseReplayData(bytes);
            replayService.saveFromApi(jsonNode, file.getOriginalFilename(), "api");
        }
        return "성공";
    }

    // 14.10 패치 이전
    @PostMapping("/parseFromApiBefore")
    public String parseFromApiBefore(@RequestBody List<MultipartFile> files) throws IOException, Exception {
        for(MultipartFile file : files) {
            byte[] bytes = parseService.changeByteArray(file.getInputStream());
            JsonNode jsonNode = parseService.parseReplayDataBefore(bytes);
            replayService.saveFromApi(jsonNode, file.getOriginalFilename(), "api");
        }
        return "성공";
    }
}
