package com.stats.lolgg.model;

import lombok.Data;

@Data
public class LeagueSearchDTO {
    private String game_id;
    private String riot_name;
    private String champ_name;
    private String position;
    private int kill;
    private int death;
    private int assist;
    private String game_result;
    private String game_team;

    private int total_damage_champions;
    private int vision_bought;
}
