package com.stats.lolgg.model;

import lombok.Data;

@Data
public class LeagueStatsVO {
    private String riot_name;
    private String position;
    private String champ_name;
    private int total_count;
    private int win;
    private int lose;
    private float win_rate;
    private float kda;

    public LeagueStatsVO(){}

    @Override
    public String toString() {
        return "LeagueStatsVO [riot_name=" + riot_name + ", position=" + position + ", champ_name=" + champ_name
                + ", total_count=" + total_count + ", win=" + win + ", lose=" + lose + ", win_rate=" + win_rate + "]";
    }
}
