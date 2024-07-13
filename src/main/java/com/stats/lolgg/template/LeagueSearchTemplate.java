package com.stats.lolgg.template;

import java.util.List;

import com.stats.lolgg.model.LeagueSearchDTO;

import net.dv8tion.jda.api.EmbedBuilder;

/** 
 * 검색 템플릿
 * @author: codingpoppy94
 * @version 1.0
 */
public class LeagueSearchTemplate {

    public EmbedBuilder build(List<LeagueSearchDTO> records){
        EmbedBuilder embed = new EmbedBuilder();
        
        LeagueSearchDTO dto = records.get(0);
        String header = dto.getGame_id();

        embed.setTitle(header);
        
        embed.addField(setFieldHeader(dto, "blue"), builder(records,"blue"), false);
        embed.addField(setFieldHeader(dto, "red"), builder(records,"red"), false);
        
        return embed;
    }

    private String builder(List<LeagueSearchDTO> list, String team){
        StringBuilder builder = new StringBuilder();
        for(LeagueSearchDTO record : list ){
            if("blue".equals(team)){
                if("blue".equals(record.getGame_team())){
                    builder.append(" "+ record.getRiot_name() +"   "+ record.getChamp_name() +" "+ record.getKill() +"/"+ record.getDeath() +"/"+ record.getAssist() + " 피해량: " + record.getTotal_damage_champions() +" 핑와: "+ record.getVision_bought() +"\n ");
                } 
            } else {
                if("red".equals(record.getGame_team())){
                    builder.append(" "+ record.getRiot_name() +"   "+ record.getChamp_name() +" "+ record.getKill() +"/"+ record.getDeath() +"/"+ record.getAssist() + " 피해량: " + record.getTotal_damage_champions() +" 핑와: "+ record.getVision_bought() +"\n " );
                } 
            }
        }
        return builder.toString();
    }

    private String setFieldHeader(LeagueSearchDTO dto, String team){
        String result = "";
        if("blue".equals(team)){
            result += ":blue_circle: 블루 ";
            if("blue".equals(dto.getGame_team()) && "승".equals(dto.getGame_result())){
                result += ":v:";
            } 
        } else {
            result += ":red_circle: 레드 ";
            if("blue".equals(dto.getGame_team()) && "패".equals(dto.getGame_result())){
                result += ":v:";
            } 
        }
        return result;
    }
}
