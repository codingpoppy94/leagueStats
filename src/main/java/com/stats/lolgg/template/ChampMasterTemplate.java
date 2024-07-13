package com.stats.lolgg.template;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.stats.lolgg.model.ChampMasterDTO;

import net.dv8tion.jda.api.EmbedBuilder;

/** 
 * 장인 템플릿
 * @author: codingpoppy94
 * @version 1.0
 */
public class ChampMasterTemplate {

    public EmbedBuilder build(List<ChampMasterDTO> records, String champName){
        EmbedBuilder embed = new EmbedBuilder();
        String header = champName;

        List<ChampMasterDTO> recordsOrderByWinRate = records.stream()
        .filter(stats -> stats.getWin_rate() >= 50)
        .sorted(Comparator.comparingDouble(ChampMasterDTO::getWin_rate).reversed())
        .limit(10)
        .collect(Collectors.toList());
        
        embed.setTitle(header);
        embed.addField("판수", builder(records), true);
        embed.addField("승률(50%이상)", builder(recordsOrderByWinRate), true);
        
        return embed;
    }

    private String builder(List<ChampMasterDTO> list){
        StringBuilder builder = new StringBuilder();
        for(ChampMasterDTO record : list ){
            builder.append(TemplateUtils.makeTeamStats(record.getRiot_name(),record.getWin(),record.getLose(),record.getWin_rate()));
        }
        return builder.toString();
    }
}