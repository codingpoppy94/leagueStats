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
    private String create_user;        //리플 올린 사람

    private int gold;
    private int ccing;
    private int time_played;
    private int total_damage_champions;
    private int total_damage_taken;
    private int vision_score;
    private int vision_bought;

    public LeagueVO() {}

    @Override
    public String toString() {
        return "LeagueVO [game_id=" + game_id + ", riot_name=" + riot_name + ", champ_name=" + champ_name
                + ", position=" + position + ", kill=" + kill + ", death=" + death + ", assist=" + assist
                + ", game_result=" + game_result + ", game_team=" + game_team + ", game_date=" + game_date
                + ", delete_yn=" + delete_yn + "]";
    }
}
