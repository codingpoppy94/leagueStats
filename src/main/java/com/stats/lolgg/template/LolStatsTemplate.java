package com.stats.lolgg.template;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;

public class LolStatsTemplate {

    /* !전적 템플릿 */
    public String makeRecordTemplate(Map<String,Object> map){

        String riotName = (String) map.get("riotName");
        LeagueStatsVO monthRecord = (LeagueStatsVO) map.get("monthRecord");
        List<LeagueStatsVO> allRecords = (List<LeagueStatsVO>) map.get("allRecord");
        List<LeagueVO> recentMatchs = (List<LeagueVO>) map.get("recentMatch");
        List<LeagueStatsVO> mostPicks = (List<LeagueStatsVO>) map.get("mostPick");
        List<LeagueStatsVO> goodTeams = (List<LeagueStatsVO>) map.get("goodTeam");
        List<LeagueStatsVO> badTeams = (List<LeagueStatsVO>) map.get("badTeam");
        List<LeagueStatsVO> easyRivals = (List<LeagueStatsVO>) map.get("easyRivals");
        List<LeagueStatsVO> hardRivals = (List<LeagueStatsVO>) map.get("hardRivals");
        
        int allTotal = 0;
        int allWin = 0;
        int allLose = 0;
        float allWinLate = 0;

        int maxTotalCount = 0;
        for(LeagueStatsVO record: allRecords){
            if (record.getTotal_count() > maxTotalCount) {
                maxTotalCount = record.getTotal_count();
            }
        }
        
        //통합전적
        String allStatStr = "";
        for(LeagueStatsVO record: allRecords){
            allWin += record.getWin();
            allLose += record.getLose();

            if(record.getTotal_count() ==  maxTotalCount ) {
                allStatStr += ":thumbsup: ";
            }
            allStatStr += record.getPosition()+" - " + " " + record.getTotal_count() + "전" + " ";
            allStatStr += record.getWin() + "승/" + record.getLose() + "패 - " + record.getWin_rate() + "% 승률 \n" ;
        }

        allTotal = allWin + allLose;
        allWinLate = Math.round((float) allWin * 100 / allTotal * 100) / 100.0f;

        //모스트픽
        // String mostStr = "";
        // for(LeagueStatsVO most : mostPicks) {
        //     mostStr += most.getChamp_name() + ": " + most.getTotal_count() + "회 " + most.getWin_rate() + "%\n";
        // }

        //템플릿생성
        String headerStr = "전적통계 **" + riotName +"**\n";
        headerStr += "이번달 전적 - "+ monthRecord.getTotal_count()+"";
        headerStr += "전 "+monthRecord.getWin()+"승/"+monthRecord.getLose()+"패 ";
        headerStr += ""+monthRecord.getWin_rate()+"% 승률\n\n";

        String content = "통합 전적 - "+ allTotal +"전 "+allWin+"승/"+allLose+"패 - "+allWinLate+"% 승률\n";
        content += allStatStr;
        content += "\n";

        String content2 = mergeContent2(recentMatchs, goodTeams, badTeams);
        // String content2 = "최근 "+recentTotal+"전 "+recentWin+"승 "+recentlose+"패 \t " + "**Good팀워크 \t Bad팀워크** \n";
        // content2 += recentStr;
        content2 += "\n";
        
        // String content3 = "MostPick 10\n";
        String content3 = mergeContent3(mostPicks, easyRivals, hardRivals);

        String end = "";

        String result =  headerStr + content + content2 + content3 + end;
        return result;
    }

    /* !장인 */
    public String makeChampMasterTemplate(List<LeagueStatsVO> records, String champName){
        String header = "**이번달 " +champName+ " 장인** \n\n";
        String content1 = "";

        for(LeagueStatsVO record : records ){
            String list = "";
            list += record.getRiot_name() + " ";
            list += record.getWin() + "승 " + record.getLose() + "패 " + record.getWin_rate() + "% \n";
            content1 += list;
        }
        return header + content1;
    }

    /* !통계 */
    public String makeChampHighRateTemplate(List<LeagueStatsVO> records){
        String header = "**이번달 챔프 통계(5판 이상)** \n\n";
        String content1 = "";

        for(LeagueStatsVO record : records ){
            String list = "";
            list += record.getChamp_name() + " ";
            list += record.getWin() + "승 " + record.getLose() + "패 " + record.getWin_rate() + "% \n";
            content1 += list;
        }
        return header + content1;
    }

    private String mergeContent2(List<LeagueVO> recentMatchs, List<LeagueStatsVO> goodTeams, List<LeagueStatsVO> badTeams) {
        int maxLength = Math.max(Math.max(recentMatchs.size(), goodTeams.size()), badTeams.size());
        StringBuilder sb = new StringBuilder();

        int recentTotal = 0;
        int recentWin = 0;
        int recentlose = 0;

        for (int i = 0; i < maxLength; i++) {
            if (i < recentMatchs.size()) {
                String recentStr = "";
                LeagueVO recentMatch = recentMatchs.get(i);
                recentTotal ++;
                if("승".equals(recentMatch.getGame_result())){
                    recentWin ++;
                    recentStr += ":blue_circle: ";
                }else {
                    recentlose ++;
                    recentStr += ":red_circle: ";
                }
                recentStr +=recentMatch.getChamp_name() + " " + recentMatch.getKda();
                sb.append(recentStr);
            }
            sb.append("\t");

            if (i < goodTeams.size()) {
                String goodTeamStr = "";
                LeagueStatsVO goodTeam = goodTeams.get(i);
                goodTeamStr += goodTeam.getRiot_name() + ": " + goodTeam.getWin() +"승/" + goodTeam.getLose() + "패 " + goodTeam.getWin_rate()+ "%";
                sb.append(goodTeamStr);
            }else {
                sb.append("\t\t\t\t\t\t\t");
            }
            sb.append("\t");

            if (i < badTeams.size()) {
                String badTeamStr = "";
                LeagueStatsVO badTeam = badTeams.get(i);
                badTeamStr += badTeam.getRiot_name() + ": " + badTeam.getWin() +"승/" + badTeam.getLose() + "패 " + badTeam.getWin_rate()+ "%";
                sb.append(badTeamStr);
            }
            sb.append("\n");
        }

        String header = "최근 "+recentTotal+"전 "+recentWin+"승 "+recentlose+"패 \t\t" + "**팀워크:blue_heart: \t 팀워크:broken_heart:** \n";
        sb.insert(0, header);
        return sb.toString();
    }

    private String mergeContent3(List<LeagueStatsVO> mostPicks, List<LeagueStatsVO> easyRivals, List<LeagueStatsVO> hardRivals) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Math.max(Math.max(mostPicks.size(), easyRivals.size()), hardRivals.size()); i++) {
            if (i < mostPicks.size()) {
                String mostStr = "";
                LeagueStatsVO most = mostPicks.get(i);
                mostStr += most.getChamp_name() + ": " + most.getTotal_count() + "회 " + most.getWin_rate() + "%";
                sb.append(mostStr);
            }
            sb.append("\t");

            if (i < easyRivals.size()) {
                String easyStr = "";
                LeagueStatsVO easy = easyRivals.get(i);
                easyStr += easy.getRiot_name() + ": " + easy.getWin() +"승/" + easy.getLose() + "패 " + easy.getWin_rate()+ "%";
                sb.append(easyStr);
            }else {
                sb.append("\t\t\t\t\t\t\t");
            }
            sb.append("\t");

            if (i < hardRivals.size()) {
                String hardStr = "";
                LeagueStatsVO hard = hardRivals.get(i);
                hardStr += hard.getRiot_name() + ": " + hard.getWin() +"승/" + hard.getLose() + "패 " + hard.getWin_rate()+ "%";
                sb.append(hardStr);
            }
            sb.append("\n");
        }

        String header = "MostPick 10 \t\t" + "**맞라인:thumbsup: \t 맞라인:thumbsdown: ** \n";
        sb.insert(0, header);

        return sb.toString();
    }
}

