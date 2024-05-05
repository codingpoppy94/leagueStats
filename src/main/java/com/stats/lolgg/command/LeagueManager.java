package com.stats.lolgg.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;
import com.stats.lolgg.service.LeagueService;
import com.stats.lolgg.template.LolTemplate;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;

/**
 * 본인 전적 데이터 처리 Manager
 * @author codingpoppy94
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeagueManager {

    private final LeagueService leagueService;

    public EmbedBuilder  getHelp(){
        LolTemplate template = new LolTemplate();
        return template.makeHelpTemplate();
    }

    /* !전적 */
    public EmbedBuilder getRecord(String riotName) {
        Map<String,Object> map = new HashMap<>();

        List<LeagueStatsVO> allRecord = leagueService.findRecord(riotName);
        if(allRecord.isEmpty()) {
            return null;
        }
        
        List<LeagueVO> recentMatch = leagueService.findTopTen(riotName);
        List<LeagueStatsVO> mostPick = leagueService.findMostPick(riotName);
        LeagueStatsVO monthRecord = leagueService.findRecordMonth(riotName); 
        List<LeagueStatsVO> otherTeamRecord = leagueService.findRecordOtherTeam(riotName);
        
        List<LeagueStatsVO> teamRecord = leagueService.findRecordWithTeam(riotName);

        List<LeagueStatsVO> goodTeam = teamRecord.stream().filter(t -> t.getWin_rate() >= 50).collect(Collectors.toList());
        List<LeagueStatsVO> badTeam = teamRecord.stream().filter(t -> t.getWin_rate() < 45).collect(Collectors.toList());
        Collections.reverse(badTeam);

        List<LeagueStatsVO> easyRivals = otherTeamRecord.stream().filter(t -> t.getWin_rate() >= 50).collect(Collectors.toList());
        List<LeagueStatsVO> hardRivals = otherTeamRecord.stream().filter(t -> t.getWin_rate() < 45).collect(Collectors.toList());
        Collections.reverse(hardRivals);

        map.put("riotName",riotName);
        map.put("allRecord", allRecord);
        map.put("recentMatch", recentMatch);
        map.put("mostPick", mostPick);
        map.put("monthRecord", monthRecord);
        map.put("goodTeam", goodTeam);
        map.put("badTeam", badTeam);
        map.put("easyRivals", easyRivals);
        map.put("hardRivals", hardRivals);

        LolTemplate template = new LolTemplate();

        return template.makeRecordTemplate(map);
    }

    /* !장인 {riot_champ} */
    public EmbedBuilder getChampMaster(String champName){
        List<LeagueStatsVO> records = leagueService.findChampMaster(champName);
        if(records.isEmpty()){
            return null;
        }
        LolTemplate template = new LolTemplate();

        return template.makeChampMasterTemplate(records, champName);
    }

    /* !통계  */
    public EmbedBuilder getChampHighRate(){
        List<LeagueStatsVO> records = leagueService.findChampHighRate();
        if(records.isEmpty()){
            return null;
        }
        LolTemplate template = new LolTemplate();

        return template.makeChampHighRateTemplate(records);
    }

    /* !라인 {position} */
    public EmbedBuilder getRecordLine(String position){
        String realPostion = switch (position) {
            case "탑" -> "TOP";
            case "정글" -> "JUG";
            case "미드" -> "MID";
            case "원딜" -> "ADC";
            case "서폿" -> "SUP";
            default -> throw new IllegalStateException("Unexpected value: " + position);
        };
        List<LeagueStatsVO> records = leagueService.findRecordLine(realPostion);
        if(records.isEmpty()){
            return null;
        } 
        LolTemplate template = new LolTemplate();
        return template.makeRecordLine(records, realPostion);
    }
}
