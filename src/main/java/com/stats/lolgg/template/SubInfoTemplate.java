package com.stats.lolgg.template;

import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.EmbedBuilder;

/** 
 * 부캐 목록 템플릿
 * @author: codingpoppy94
 * @version 1.0
 */
public class SubInfoTemplate {
    
    public EmbedBuilder build(List<Map<String,Object>> maps){
        EmbedBuilder embed = new EmbedBuilder();
        
        embed.setTitle("부캐목록");
        embed.setDescription(builder(maps));
        
        return embed;
    }

    private String builder(List<Map<String,Object>> maps){
        StringBuilder builder = new StringBuilder();
        builder.append("```\n");
        builder.append("|  부캐  |  본캐  |\n");
        builder.append("\n");
        for(Map<String,Object> map : maps){
            builder.append("| "+map.get("sub_name")+"  | "+map.get("main_name")+"\n");
        }
        builder.append("\n");
        builder.append("총: "+maps.size()+"\n");
        builder.append("```\n");
        return builder.toString();
    }
}
