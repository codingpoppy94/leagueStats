package com.stats.lolgg.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LeagueVO {
    
    private String id;
    private String game_id;
    private String riot_name;
    private String champ_name;
    private String game_line;
    private String kda;
    private boolean game_result;
    private String game_team;
    private LocalDateTime game_date;
    private LocalDateTime create_date;

    public LeagueVO() {}

    public LeagueVO(String id, String game_id, String riot_name, String champ_name, String game_line,
                  String kda, boolean game_result, String game_team, LocalDateTime game_date, LocalDateTime create_date) {
        this.id = id;
        this.game_id = game_id;
        this.riot_name = riot_name;
        this.champ_name = champ_name;
        this.game_line = game_line;
        this.kda = kda;
        this.game_result = game_result;
        this.game_team = game_team;
        this.game_date = game_date;
        this.create_date = create_date;
    }

    @Override
    public String toString() {
        return "League{" +
                "id='" + id + '\'' +
                ", game_id='" + game_id + '\'' +
                ", riot_name='" + riot_name + '\'' +
                ", champ_name='" + champ_name + '\'' +
                ", game_line='" + game_line + '\'' +
                ", kda='" + kda + '\'' +
                ", game_result=" + game_result +
                ", game_team='" + game_team + '\'' +
                ", game_date=" + game_date +
                ", create_date=" + create_date +
                '}';
    }
}
