package com.stats.lolgg.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;
import com.stats.lolgg.service.LeagueService;
import com.stats.lolgg.template.LolStatsTemplate;

@Component
public class LeagueManager {

    @Autowired
    private LeagueService leagueService;

    public LeagueManager () {}

    /* 
     * !전적조회
     */
    public String getRecord(String riotName) {

        Map<String,Object> map =  new HashMap<String,Object>();

        List<LeagueStatsVO> allRecord = leagueService.findRecord(riotName);
        if(allRecord.size() < 1) {
            return "";
        }
        
        List<LeagueVO> recentMatch = leagueService.findTopTen(riotName);
        List<LeagueStatsVO> mostPick = leagueService.findMostPick(riotName);
        LeagueStatsVO monthRecord = leagueService.findRecordMonth(riotName); 

        map.put("riotName",riotName);
        map.put("allRecord", allRecord);
        map.put("recentMatch", recentMatch);
        map.put("mostPick", mostPick);
        map.put("monthRecord", monthRecord);

        LolStatsTemplate template = new LolStatsTemplate();
        String result = template.makeTemplate(map);

        return result;
    }

    /* 장인 챔 Rank */
    public String getChampMaster(){
        return "";
    }

    /* 이번달 승률 좋은 챔 */
    public String getChampHighRate(){
        return "";
    }
}
