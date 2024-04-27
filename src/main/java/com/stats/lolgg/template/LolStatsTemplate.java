package com.stats.lolgg.template;

import java.util.List;
import java.util.Map;

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

        //최근전적
        String recentStr = "";
        int recentTotal = 0;
        int recentWin = 0;
        int recentlose = 0;
        for(LeagueVO recent : recentMatchs) {
            recentTotal ++;
            if("승".equals(recent.getGame_result())){
                recentWin ++;
                recentStr += ":blue_circle: ";
            }else {
                recentlose ++;
                recentStr += ":red_circle: ";
            }
            recentStr +=   recent.getChamp_name() + " " + recent.getKda() + "\n";
        }

        //모스트픽
        String mostStr = "";
        for(LeagueStatsVO most : mostPicks) {
            mostStr += most.getChamp_name() + ": " + most.getTotal_count() + "회 " + most.getWin_rate() + "\n";
        }

        //템플릿생성
        String headerStr = "전적통계 **" + riotName +"**\n";
        headerStr += "이번달 전적 - "+ monthRecord.getTotal_count()+"";
        headerStr += "전 "+monthRecord.getWin()+"승/"+monthRecord.getLose()+"패 ";
        headerStr += ""+monthRecord.getWin_rate()+"% 승률\n\n";

        String content = "통합 전적 - "+ allTotal +"전 "+allWin+"승/"+allLose+"패 "+allWinLate+"% 승률\n";
        content += allStatStr;
        content += "\n";

        String content2 = "최근 "+recentTotal+"전 "+recentWin+"승 "+recentlose+"패\n" ;
        content2 += recentStr;
        content2 += "\n";
        
        String content3 = "MostPick 10\n";
        content3 += mostStr;

        String end = "";

        String result =  headerStr + content + content2 + content3+ end;
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
}

