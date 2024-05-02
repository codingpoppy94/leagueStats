package com.stats.lolgg.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;
import com.stats.lolgg.service.LeagueService;
import com.stats.lolgg.template.LolTemplate;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Component
public class LeagueManager {

    @Autowired
    private LeagueService leagueService;

    public LeagueManager () {}

    public EmbedBuilder  getHelp(){
        LolTemplate template = new LolTemplate();
        return template.makeHelpTemplate();
    }

    /* !전적 */
    public EmbedBuilder getRecord(String riotName) {
        Map<String,Object> map =  new HashMap<String,Object>();

        List<LeagueStatsVO> allRecord = leagueService.findRecord(riotName);
        if(allRecord.size() < 1) {
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
        EmbedBuilder result = template.makeRecordTemplate(map);

        return result;
    }

    /* !장인 {riot_champ} */
    public EmbedBuilder getChampMaster(String champName){
        List<LeagueStatsVO> records = leagueService.findChampMaster(champName);
        if(records.size() < 1){
            return null;
        }
        LolTemplate template = new LolTemplate();
        EmbedBuilder result = template.makeChampMasterTemplate(records, champName);

        return result;
    }

    /* !통계  */
    public EmbedBuilder getChampHighRate(){
        List<LeagueStatsVO> records = leagueService.findChampHighRate();
        if(records.size() < 1){
            return null;
        }
        LolTemplate template = new LolTemplate();
        EmbedBuilder result = template.makeChampHighRateTemplate(records);
        
        return result;
    }

    /* !라인 {position} */
    public EmbedBuilder getRecordLine(String position){
        String realPostion = "";
        switch (position) {
            case "탑": 
            realPostion = "TOP";
            break;
            case "정글":
            realPostion = "JUG";
            break;
            case "미드":
            realPostion = "MID";
            break;
            case "원딜":
            realPostion = "ADC";
            break;
            case "서폿":
            realPostion = "SUP";
            break;
        
            default:
            realPostion = "TOP";
            break;
        }
        List<LeagueStatsVO> records = leagueService.findRecordLine(realPostion);
        if(records.size() < 1){
            return null;
        } 
        LolTemplate template = new LolTemplate();
        EmbedBuilder result = template.makeRecordLine(records, realPostion);
        return result;
    }
}
