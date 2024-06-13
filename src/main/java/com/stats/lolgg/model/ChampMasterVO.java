package com.stats.lolgg.model;

import lombok.Data;

/** 
 * 장인 vo
 * @author codingpoppy94
 * @version 1.0
 */
@Data
public class ChampMasterVO {
    private String riot_name;
    private String champ_name;
    private int total_count;
    private int win;
    private int lose;
    private float win_rate;
    private float kda;
    
}
