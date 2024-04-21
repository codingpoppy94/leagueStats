package com.stats.lolgg.model;

public class ReplaysModel {
    private String ASSISTS; //어시스트
    private String NUM_DEATHS;  //데스
    private String CHAMPIONS_KILLED; //킬
    private String TEAM_POSITION; //포지션
    private String NAME; //닉네임
    private String WIN; //게임 승/패
    private String SKIN; //챔피언 이름

    // Getter 및 Setter 메서드
    public String getAssists() {
        return ASSISTS;
    }

    public void setAssists(String ASSISTS) {
        this.ASSISTS = ASSISTS;
    }

    public String getNumDeaths() {
        return NUM_DEATHS;
    }

    public void setNumDeaths(String NUM_DEATHS) {
        this.NUM_DEATHS = NUM_DEATHS;
    }

    public String getChampionsKilled() {
        return CHAMPIONS_KILLED;
    }

    public void setChampionsKilled(String CHAMPIONS_KILLED) {
        this.CHAMPIONS_KILLED = CHAMPIONS_KILLED;
    }

    public String getTeamPosition() {
        return TEAM_POSITION;
    }

    public void setTeamPosition(String TEAM_POSITION) {
        this.TEAM_POSITION = TEAM_POSITION;
    }

    public String getName() {
        return NAME;
    }

    public void setName(String NAME) {
        this.NAME = NAME;
    }

    public String getWin() {
        return WIN;
    }

    public void setWin(String WIN) {
        this.WIN = WIN;
    }

    public String getSkin() {
        return SKIN;
    }

    public void setSkin(String SKIN) {
        this.SKIN = SKIN;
    }
}
