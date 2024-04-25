package com.stats.lolgg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;
import com.stats.lolgg.service.LeagueService;



@RestController
@RequestMapping("/league")
public class LeagueController {

    @Autowired
    private LeagueService leagueService;

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
    public List<LeagueVO> getRecord(@RequestParam String riot_name){
        return leagueService.findRecord(riot_name);
    }

    @GetMapping("/getRecordMonth")
    public List<LeagueStatsVO> getRecordMonth(@RequestParam String riot_name){
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

    @GetMapping("/getChampHighRate")
    public List<LeagueStatsVO> getChampHighRate(){
        return leagueService.findChampHighRate();
    }

    /**
     * Replay 데이터 파싱>저장
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/replay")
    public String getReplayFile(@RequestBody List<MultipartFile> files) throws Exception {
        for(MultipartFile file : files ){
            String fileName = file.getOriginalFilename();
            // System.out.println(fileName);
            
            leagueService.saveAll(file);
        }
        return "성공";
    }
}
