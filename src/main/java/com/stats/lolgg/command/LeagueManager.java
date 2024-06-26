package com.stats.lolgg.command;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * 통계데이터 event 호출 Manager
 * @author codingpoppy94
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeagueManager {

    private final LeagueService leagueService;

    public EmbedBuilder getHelp(){
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

    /* !통계 챔피언 */
    public EmbedBuilder getChampStats(){
        // 서버의 현재 시간
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul")); 
        LocalDateTime previousMonth = now.minus(1, ChronoUnit.MONTHS);
        
        int month = now.getMonthValue();
        int year = now.getYear();
        int lastmonth = previousMonth.getMonthValue();
        int lastyear = previousMonth.getMonthValue();

        List<LeagueStatsVO> records = leagueService.findChampStats(year, month);
        List<LeagueStatsVO> lastMonthRecords = leagueService.findChampStats(lastyear, lastmonth);

        Map<String, List<LeagueStatsVO>> resultMap = new HashMap<>();
        resultMap.put("thisMonth", records);
        resultMap.put("lastMonth", lastMonthRecords);

        if(records.isEmpty() && lastMonthRecords.isEmpty()){
            return null;
        }
        LolTemplate template = new LolTemplate();

        return template.makeChampRateTemplate(resultMap);
    }

    /* !통계 게임 */
    public EmbedBuilder getGamesStats(){
        // 서버의 현재 시간
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul")); 
        int month = now.getMonthValue();
        int year = now.getYear();
        List<LeagueStatsVO> leagueGames = leagueService.groupLeagueByRiotName(year, month);

        Map<String, List<LeagueStatsVO>> resultMap = new HashMap<>();
        resultMap.put("leagueGames", leagueGames);

        if(leagueGames.isEmpty()){
            return null;
        }
        LolTemplate template = new LolTemplate();

        return template.makeGamesTeamplte(resultMap);
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

    /* !클랜통계 {year-month} 비밀통계 */
    public String getClanStats(String dates){

        String[] date = dates.split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        // 서버의 현재 시간
        List<LeagueStatsVO> leagueGames = leagueService.groupLeagueByRiotName(year, month);

        Map<String, List<LeagueStatsVO>> resultMap = new HashMap<>();
        resultMap.put("leagueGames", leagueGames);

        if(leagueGames.isEmpty()){
            return null;
        }
        LolTemplate template = new LolTemplate();
        return template.makeClanStatstemplate(resultMap, year, month);
    }
}
