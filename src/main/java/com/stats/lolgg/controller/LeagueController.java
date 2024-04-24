package com.stats.lolgg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
