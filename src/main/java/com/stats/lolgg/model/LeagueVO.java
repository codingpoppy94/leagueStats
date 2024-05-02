package com.stats.lolgg.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LeagueVO {
    
    private String id;
    private String game_id;
    private String riot_name;
    private String champ_name;
    private String position;
    private int kill;
    private int death;
    private int assist;
    private String game_result;
    private String game_team;
    private LocalDateTime game_date;
    private LocalDateTime create_date;
    private char delete_yn;

    public LeagueVO() {}

    @Override
    public String toString() {
        return "LeagueVO [game_id=" + game_id + ", riot_name=" + riot_name + ", champ_name=" + champ_name
                + ", position=" + position + ", kill=" + kill + ", death=" + death + ", assist=" + assist
                + ", game_result=" + game_result + ", game_team=" + game_team + ", game_date=" + game_date
                + ", delete_yn=" + delete_yn + "]";
    }
}
