package com.stats.lolgg.template;

import com.fasterxml.jackson.databind.JsonNode;

public class LolStatsTemplate {

    public String makeTemplate(JsonNode dataNode){
        
        JsonNode monthRecord = dataNode.get("monthRecord");
        JsonNode allRecord = dataNode.get("allRecord");
        JsonNode recentMatch = dataNode.get("recentMatch");


        String header = "``` \n";

        String str1 = "**" + monthRecord.get("riot_name") +"** \n";
        str1 += "이번달 전적 - ( " + monthRecord.get("total_count") +")";
        str1 += "/전 ("+monthRecord.get("win")+")승/("+monthRecord.get("lose")+")패";
        str1 += "("+monthRecord.get("win_rate")+")승률\n\n";

        String str2 = "통합 전적 - (total_count)/전 (win)승/(lose)패 (win_rate)승률\n\n";
        str2 += "FOR문\n";

        String str3 = "최근 (total_count)전 (win)승 (lose)패 \n" ;
        str3 += "for문";

        

        String end = "```";

        





        return "";
    }
    
}
