package com.stats.lolgg.template;

import java.util.List;

import com.stats.lolgg.model.LeagueStatsVO;

public class TemplateUtils {
    public static String makeStats(String prefix, int totalCount, int win, int lose, float win_rate, float kda){
        String stats = prefix + " - " + totalCount+"전 "+win+"승/"+lose+"패  "+win_rate+"% 승률";
        if(kda != 9999){
            stats += " KDA: " + kda;
        }
        stats += "\n";
        return stats;
    }

    public static String makeTeamStats(String prefix, int win, int lose, float win_rate){
        return prefix + ": " + win +"승/" + lose + "패 " + win_rate+ "%\n";
    }

    public static String makeStatsList(List<LeagueStatsVO> list, String type){
        StringBuilder builder = new StringBuilder();
        int i = 1;
        if("champ".equals(type)){
            for(LeagueStatsVO vo : list){
                builder.append(i++ +". " + makeTeamStats(vo.getChamp_name(), vo.getWin(), vo.getLose(), vo.getWin_rate()));
            }
        }
        if("riotname".equals(type)){
            for(LeagueStatsVO vo : list){
                // builder.append(i++ +". " + makeStats(vo.getRiot_name(), vo.getTotal_count() ,vo.getWin(), vo.getLose(), vo.getWin_rate(), vo.getKda()));
                builder.append(i++ +". " + hideStats(vo.getRiot_name(), vo.getWin(), vo.getWin_rate(), vo.getKda()));
            }
        }
        return builder.toString();
    }

    public static String hideStats(String prefix, int win, float win_rate, float kda){
        String stats = prefix + " - " + +win+"승/"+win_rate+"%";
        if(kda != 9999){
            stats += " KDA: " + kda;
        }
        stats += "\n";
        return stats;
    }
}
